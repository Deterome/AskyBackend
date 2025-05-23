package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.service.UserService;
import org.senla_project.application.service.linker.UserLinkerService;
import org.senla_project.application.util.data.DefaultRole;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.ForbiddenException;
import org.senla_project.application.util.security.AuthenticationManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private RoleMapper roleMapper;
    final private UserMapper userMapper;
    final private PasswordEncoder passwordEncoder;
    final private UserLinkerService userLinkerService;

    @Transactional(readOnly = true)
    private Set<Role> getDefaultRolesSet() {
        return roleMapper.toRoleSetFromStringList(List.of(DefaultRole.USER.toString()));
    }

    @Transactional
    @Override
    public UserResponseDto create(@NonNull UserCreateDto element) {
        User user = userMapper.toUser(element);
        user.setRoles(getDefaultRolesSet());
        user.setPassword(passwordEncoder.encode(element.getPassword()));
        userLinkerService.linkUserWithRoles(user);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserResponseDto update(@NonNull UserUpdateDto userUpdateDto) throws EntityNotFoundException {
        Optional<User> oldUser = userRepository.findById(UUID.fromString(userUpdateDto.getUserId()));
        if (oldUser.isEmpty()) throw new EntityNotFoundException("User not found");
        if (AuthenticationManager.ifUsernameBelongsToAuthenticatedUser(oldUser.get().getUsername())
                || AuthenticationManager.isAuthenticatedUserAnAdmin()) {
            User updatedUser = userMapper.toUser(userUpdateDto);
            if (updatedUser.getPassword() != null) updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            if (updatedUser.getRoles() != null) userLinkerService.linkUserWithRoles(updatedUser);
            return userMapper.toUserResponseDto(userRepository.save(userMapper.partialUserToUser(oldUser.get(), updatedUser)));
        } else {
            throw new ForbiddenException(String.format("You are not %s! You can't update this user!", oldUser.get().getUsername()));
        }
    }

    @Transactional
    @Override
    @PreAuthorize("#deletedUser.username == authentication.getName() or hasAuthority('admin')")
    public void delete(@NonNull @P("deletedUser") UserDeleteDto deleteDto) {
        userRepository.deleteByUsername(deleteDto.getUsername());
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
    @Override
    public UserResponseDto getByUsername(@NonNull String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));

    }

    @Transactional(readOnly = true)
    @Override
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

}