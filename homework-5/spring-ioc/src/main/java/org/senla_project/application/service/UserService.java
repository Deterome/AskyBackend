package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService implements ServiceInterface<UserCreateDto, UserResponseDto> {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserMapper userMapper;

    @Override
    public void execute() {}

    @Override
    public List<UserResponseDto> getAllElements() {
        return userMapper.toUserResponseDtoList(userDao.findAll());
    }

    @Override
    public Optional<UserResponseDto> getElementById(@NonNull UUID id) {
        return userDao.findById(id)
                .map(userMapper::toUserResponseDto);
    }

    @Override
    public void addElement(@NonNull UserCreateDto element) {
        userDao.create(userMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull UserCreateDto updatedElement) {
        userDao.update(id, userMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        userDao.deleteById(id);
    }

    public Optional<UUID> findUserId(@NonNull String nickname) {
        return userDao.findUserByNickname(nickname).map(Entity::getId);
    }

}
