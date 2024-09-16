package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig({DataSourceConfigTest.class, HibernateConfigTest.class, UserRepositoryImpl.class})
@Transactional
class UserRepositoryImplTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void create() {
        User expectedUser = TestData.getUser();
        userRepository.create(expectedUser);
        User actual = userRepository.findById(expectedUser.getUserId()).get();
        Assertions.assertEquals(expectedUser, actual);
    }

    @Test
    void findById() {
        User expectedUser = TestData.getUser();
        userRepository.create(expectedUser);
        User actual = userRepository.findById(expectedUser.getUserId()).get();
        Assertions.assertEquals(expectedUser, actual);
    }

    @Test
    void findAll() {
        User user = TestData.getUser();
        List<User> expectedUserList = new ArrayList<>();
        expectedUserList.add(user);
        userRepository.create(user);
        List<User> actualUserList = userRepository.findAll();
        Assertions.assertEquals(expectedUserList, actualUserList);
    }

    @Test
    void update() {
        User user = TestData.getUser();
        userRepository.create(user);
        User expectedUser = TestData.getUpdatedUser();
        expectedUser.setUserId(user.getUserId());
        userRepository.update(expectedUser);

        User actual = userRepository.findById(expectedUser.getUserId()).get();
        Assertions.assertEquals(expectedUser, actual);
    }

    @Test
    void deleteById() {
        User user = TestData.getUser();
        userRepository.create(user);
        var userId = user.getUserId();
        userRepository.deleteById(userId);
        Optional<User> actual = userRepository.findById(userId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findUserByNickname() {
        User expectedUser = TestData.getUser();
        userRepository.create(expectedUser);
        User actual = userRepository.findUserByNickname(expectedUser.getNickname()).get();
        Assertions.assertEquals(expectedUser, actual);
    }
}