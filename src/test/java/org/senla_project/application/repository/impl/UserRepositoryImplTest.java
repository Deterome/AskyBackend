package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@SpringJUnitWebConfig({DataSourceConfigTest.class, HibernateConfigTest.class, UserRepositoryImpl.class, RoleRepositoryImpl.class})
@Transactional
class UserRepositoryImplTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void insertRolesToDataBase() {
        roleRepository.create(TestData.getRole());
    }
    User addDependenciesToUser(User user) {
        user.setRoles(user.getRoles().stream()
                .map(Role::getRoleName)
                .map(roleRepository::findRoleByName)
                .map(roleOpt -> roleOpt.orElseThrow(() -> new EntityNotFoundException("role not found")))
                .collect(Collectors.toSet()));
        return user;
    }

    @Test
    void create() {
        User expectedUser = addDependenciesToUser(TestData.getUser());
        userRepository.create(expectedUser);
        User actual = userRepository.findById(expectedUser.getUserId()).get();
        Assertions.assertEquals(expectedUser, actual);
    }

    @Test
    void findById() {
        User expectedUser = addDependenciesToUser(TestData.getUser());
        userRepository.create(expectedUser);
        User actual = userRepository.findById(expectedUser.getUserId()).get();
        Assertions.assertEquals(expectedUser, actual);
    }

    @Test
    void findAll() {
        User user = addDependenciesToUser(TestData.getUser());
        List<User> expectedUserList = new ArrayList<>();
        expectedUserList.add(user);
        userRepository.create(user);
        List<User> actualUserList = userRepository.findAll();
        Assertions.assertEquals(expectedUserList, actualUserList);
    }

    @Test
    void update() {
        User user = addDependenciesToUser(TestData.getUser());
        userRepository.create(user);
        User expectedUser = addDependenciesToUser(TestData.getUpdatedUser());
        expectedUser.setUserId(user.getUserId());
        userRepository.update(expectedUser);

        User actual = userRepository.findById(expectedUser.getUserId()).get();
        Assertions.assertEquals(expectedUser, actual);
    }

    @Test
    void deleteById() {
        User user = addDependenciesToUser(TestData.getUser());
        userRepository.create(user);
        var userId = user.getUserId();
        userRepository.deleteById(userId);
        Optional<User> actual = userRepository.findById(userId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findUserByUsername() {
        User expectedUser = addDependenciesToUser(TestData.getUser());
        userRepository.create(expectedUser);
        User actual = userRepository.findUserByUsername(expectedUser.getUsername()).get();
        Assertions.assertEquals(expectedUser, actual);
    }
}