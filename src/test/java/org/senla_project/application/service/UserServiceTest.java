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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
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
    void create() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        Mockito.when(roleService.getByRoleName(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(userMapperSpy.toUser(userCreateDto)).thenReturn(TestData.getAuthenticatedUser());
        userServiceMock.create(userCreateDto);
        Mockito.verify(userRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(userMapperSpy.toUser(id, userCreateDto)).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(userRepositoryMock.existsById(id)).thenReturn(true);
        userServiceMock.updateById(id, userCreateDto);
        Mockito.verify(userRepositoryMock).save(Mockito.any());
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(userRepositoryMock).deleteById(Mockito.any());
        userServiceMock.deleteById(UUID.randomUUID());
        Mockito.verify(userRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(userRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getUser()),
                            PageRequest.of(0, 5),
                            1));
            userServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(userRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getById() {
        try {
            userServiceMock.getById(UUID.randomUUID());
            Mockito.verify(userRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getByUsername() {
        try {
            User user = TestData.getAuthenticatedUser();
            userServiceMock.getByUsername(user.getUsername());
            Mockito.verify(userRepositoryMock).findByUsername(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}