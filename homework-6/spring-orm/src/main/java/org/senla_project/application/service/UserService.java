package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService implements ServiceInterface<UserCreateDto, UserResponseDto> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull UserCreateDto element) {
        userRepository.create(userMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull UserCreateDto updatedElement) {
        userRepository.update(id, userMapper.toEntity(updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<UserResponseDto> getAllElements() {
        return userMapper.toUserResponseDtoList(userRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<UserResponseDto> getElementById(@NonNull UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponseDto);
    }

    @Transactional
    public Optional<UUID> findUserId(@NonNull String nickname) {
        return userRepository.findUserByNickname(nickname).map(Entity::getId);
    }

}
