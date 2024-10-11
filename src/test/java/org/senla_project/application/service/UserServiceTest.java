package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.RoleMapperImpl;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepositoryMock;
    @Mock
    UserMapper userMapperSpy;
    @Spy
    RoleMapperImpl roleMapper;
    @Spy
    PasswordEncoder passwordEncoder;
    @Mock
    RoleService roleService;
    @InjectMocks
    UserService userServiceMock;

    @Test
    void addElement() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        Mockito.when(roleService.findRoleByName(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userMapperSpy.toUser(userCreateDto)).thenReturn(TestData.getAuthenticatedUser());
        userServiceMock.addElement(userCreateDto);
        Mockito.verify(userRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(userMapperSpy.toUser(id, userCreateDto)).thenReturn(TestData.getAuthenticatedUser());
        userServiceMock.updateElement(id, userCreateDto);
        Mockito.verify(userRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(userRepositoryMock).deleteById(Mockito.any());
        userServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(userRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void findAllElements() {
        try {
            userServiceMock.findAllElements();
            Mockito.verify(userRepositoryMock).findAll();
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void findElementById() {
        try {
            userServiceMock.findElementById(UUID.randomUUID());
            Mockito.verify(userRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void findUserByUsername() {
        try {
            User user = TestData.getAuthenticatedUser();
            userServiceMock.findUserByUsername(user.getUsername());
            Mockito.verify(userRepositoryMock).findUserByUsername(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}