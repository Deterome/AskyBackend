package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.UserDto;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.RoleListMapper;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.mapper.UserListMapper;
import org.senla_project.application.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserService implements ServiceInterface<UserDto> {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserListMapper userListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<UserDto> getAllElements() {
        return userListMapper.toDtoList(userDao.findAll());
    }

    @Override
    @Nullable
    public UserDto getElementById(@NonNull UUID id) {
        User user = userDao.findById(id);
        if (user == null) return null;
        return userMapper.toDto(user);
    }

    @Override
    public void addElement(@NonNull UserDto element) {
        userDao.create(userMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull UserDto updatedElement) {
        userDao.update(id, userMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull UserDto element) {
        userDao.deleteById(element.getUserId());
    }

}
