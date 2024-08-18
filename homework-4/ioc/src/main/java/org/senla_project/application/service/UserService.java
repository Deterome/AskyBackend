package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.RoleDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dao.QuestionDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dto.UserDto;
import org.senla_project.application.db.entities.User;
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

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<UserDto> getAllElements() {
        return userDao.findAll().stream().map(UserDto::new).toList();
    }

    @Override
    @Nullable
    public UserDto getElementById(@NonNull UUID id) {
        var user = userDao.findById(id);
        if (user == null) return null;
        return new UserDto(user);
    }

    @Override
    public void addElement(@NonNull UserDto element) {
        User newElement = User.builder()
                .role(roleDao.findRoleByName(element.getRoleName()))
            .build();
        userDao.create(newElement);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull UserDto updatedElement) {
        User updatedUser = User.builder()
                .role(roleDao.findRoleByName(updatedElement.getRoleName()))
            .build();
        updatedUser.setId(updatedElement.getUserId());
        userDao.update(id, updatedUser);
    }

    @Override
    public void deleteElement(@NonNull UserDto element) {
        userDao.deleteById(element.getUserId());
    }

}
