package org.senla_project.application.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.util.enums.RolesEnum;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements ServiceInterface<UUID, UserCreateDto, UserResponseDto>, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    private Set<Role> getDefaultRolesSet() {
        return roleMapper.toRoleSetFromStringList(List.of(RolesEnum.USER.toString()));
    }

    @Transactional
    @Override
    public UserResponseDto addElement(@NonNull UserCreateDto element) {
        User user = userMapper.toUser(element);
        user.setRoles(getDefaultRolesSet());
        user.setPassword(passwordEncoder.encode(element.getPassword()));
        return userMapper.toUserResponseDto(userRepository.create(
                addDependenciesToUser(user)
        ));
    }

    @Transactional
    @Override
    public UserResponseDto updateElement(@NonNull UUID id, @NonNull UserCreateDto updatedElement) {
        return userMapper.toUserResponseDto(userRepository.update(
                addDependenciesToUser(userMapper.toUser(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAllElements() throws EntityNotFoundException {
        var elements = userMapper.toUserResponseDtoList(userRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Users not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserByUsername(@NonNull String username) throws EntityNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(userMapper::toUserResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));

    }

    @Transactional(readOnly = true)
    public boolean isUserExist(String username) {
        return userRepository.findUserByUsername(username).isPresent();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(username);
        log.debug("finding user");
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        log.debug(user.toString());
        log.debug(user.getRoles().toString());
        log.debug("returning user details");
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
        for (var roleName: roleNameList) {
            roleSet.add(roleMapper.toRole(
                    roleService.findRoleByName(roleName)
            ));
        }
        user.setRoles(roleSet);
        log.debug(user.getRoles().toString());
        return user;
    }

}
