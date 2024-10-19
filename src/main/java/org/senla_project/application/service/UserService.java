package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.enums.RolesEnum;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements ServiceInterface<UUID, UserCreateDto, UserResponseDto>, UserDetailsService {

    final private UserRepository userRepository;
    final private RoleService roleService;
    final private RoleMapper roleMapper;
    final private UserMapper userMapper;
    final private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    private Set<Role> getDefaultRolesSet() {
        return roleMapper.toRoleSetFromStringList(List.of(RolesEnum.USER.toString()));
    }

    @Transactional
    @Override
    public UserResponseDto create(@NonNull UserCreateDto element) {
        User user = userMapper.toUser(element);
        user.getRoles().addAll(getDefaultRolesSet());
        user.setPassword(passwordEncoder.encode(element.getPassword()));
        return userMapper.toUserResponseDto(userRepository.save(
                addDependenciesToUser(user)
        ));
    }

    @Transactional
    @Override
    public UserResponseDto updateById(@NonNull UUID id, @NonNull UserCreateDto updatedElement) throws EntityNotFoundException {
        if (!userRepository.existsById(id)) throw new EntityNotFoundException("User not found");
        return userMapper.toUserResponseDto(userRepository.save(
                addDependenciesToUser(userMapper.toUser(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = userRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("User not found");
        return elements.map(userMapper::toUserResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponseDto getByUsername(@NonNull String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));

    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    private User addDependenciesToUser(User user) {
        List<String> roleNameList = user.getRoles().stream()
                .map(Role::getRoleName).toList();
        Set<Role> roleSet = new HashSet<>();
        for (var roleName : roleNameList) {
            roleSet.add(roleMapper.toRole(
                    roleService.getByRoleName(roleName)
            ));
        }
        user.setRoles(roleSet);
        return user;
    }

}