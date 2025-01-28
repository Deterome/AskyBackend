package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.service.impl.UserServiceImpl;
import org.senla_project.application.service.linker.UserLinkerService;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepositoryMock;
    @Mock
    UserMapper userMapperSpy;

    @Spy
    RoleMapper roleMapper;

    @Mock
    UserLinkerService userLinkerService;

    @Spy
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceMock;

    void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Alex");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void create() {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        when(userMapperSpy.toUser(userCreateDto)).thenReturn(TestData.getAuthenticatedUser());
        userServiceMock.create(userCreateDto);
        verify(userRepositoryMock).save(any());
    }

    @Test
    void update() {
        mockSecurityContext();

        UserUpdateDto userUpdateDto = TestData.getUserUpdateDto();
        UUID id = UUID.randomUUID();
        userUpdateDto.setUserId(id.toString());

        when(userMapperSpy.toUser(userUpdateDto)).thenReturn(TestData.getUpdatedUser());
        when(userRepositoryMock.findById(id)).thenReturn(Optional.of(TestData.getUser()));

        userServiceMock.update(userUpdateDto);

        verify(userRepositoryMock).save(any());
    }

    @Test
    void delete() {
        UserDeleteDto userDeleteDto = UserDeleteDto.builder()
                .username("Alex")
                .build();
        doNothing().when(userRepositoryMock).deleteByUsername(any());
        userServiceMock.delete(userDeleteDto);
        verify(userRepositoryMock).deleteByUsername(any());
    }

    @Test
    void getAll() {
        try {
            when(userRepositoryMock.findAll((Pageable) any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getUser()),
                            PageRequest.of(0, 5),
                            1));
            userServiceMock.getAll(PageRequest.of(0, 5));
            verify(userRepositoryMock).findAll((Pageable) any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getById() {
        try {
            userServiceMock.getById(UUID.randomUUID());
            verify(userRepositoryMock).findById(any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getByUsername() {
        try {
            User user = TestData.getAuthenticatedUser();
            userServiceMock.getByUsername(user.getUsername());
            verify(userRepositoryMock).findByUsername(any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}