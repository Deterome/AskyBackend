package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.TestData;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;
    @Spy
    UserMapper userMapperSpy;
    @InjectMocks
    UserService userServiceMock;

    @Test
    void addElement() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        Mockito.doNothing().when(userRepositoryMock).create(Mockito.any());
        userServiceMock.addElement(userCreateDto);
        Mockito.verify(userRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        Mockito.doNothing().when(userRepositoryMock).update(Mockito.any());
        userServiceMock.updateElement(UUID.randomUUID(), userCreateDto);
        Mockito.verify(userRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(userRepositoryMock).deleteById(Mockito.any());
        userServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(userRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAllElements() {
        userServiceMock.getAllElements();
        Mockito.verify(userRepositoryMock).findAll();
    }

    @Test
    void findElementById() {
        userServiceMock.findElementById(UUID.randomUUID());
        Mockito.verify(userRepositoryMock).findById(Mockito.any());
    }

    @Test
    void findUser() {
        User user = TestData.getUser();
        userServiceMock.findUser(user.getNickname());
        Mockito.verify(userRepositoryMock).findUserByNickname(Mockito.any());
    }
}