package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements ServiceInterface<UUID, UserCreateDto, UserResponseDto> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public UserResponseDto addElement(@NonNull UserCreateDto element) {
        return userMapper.toResponseDto(userRepository.create(userMapper.toEntity(element)));
    }

    @Transactional
    @Override
    public UserResponseDto updateElement(@NonNull UUID id, @NonNull UserCreateDto updatedElement) {
        return userMapper.toResponseDto(userRepository.update(userMapper.toEntity(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllElements() throws EntityNotFoundException {
        var elements = userMapper.toUserResponseDtoList(userRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Users not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUserByName(@NonNull String nickname) throws EntityNotFoundException {
        return userRepository.findUserByNickname(nickname)
                .map(userMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
