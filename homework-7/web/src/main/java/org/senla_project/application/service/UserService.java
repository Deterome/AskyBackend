package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements ServiceInterface<UUID, UserCreateDto, UserResponseDto> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public void addElement(@NonNull UserCreateDto element) {
        userRepository.create(userMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull UserCreateDto updatedElement) {
        userRepository.update(userMapper.toEntity(id, updatedElement));
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
    public Optional<UserResponseDto> findElementById(@NonNull UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto);
    }

    @Transactional
    public Optional<UserResponseDto> findUser(@NonNull String nickname) {
        return userRepository.findUserByNickname(nickname)
                .map(userMapper::toResponseDto);
    }

}
