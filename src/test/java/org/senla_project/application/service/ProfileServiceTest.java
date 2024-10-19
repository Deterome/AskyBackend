package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    ProfileRepository profileRepositoryMock;
    @Mock
    ProfileMapper profileMapper;
    @Spy
    UserMapper userMapper;
    @Mock
    UserService userService;
    @InjectMocks
    ProfileService profileServiceMock;

    @Test
    void create() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        Mockito.when(profileMapper.toProfile(profileCreateDto)).thenReturn(TestData.getProfile());
        Mockito.when(userMapper.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        profileServiceMock.create(profileCreateDto);
        Mockito.verify(profileRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(profileMapper.toProfile(id, profileCreateDto)).thenReturn(TestData.getProfile());
        Mockito.when(userMapper.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(profileRepositoryMock.existsById(id)).thenReturn(true);
        profileServiceMock.updateById(id, profileCreateDto);
        Mockito.verify(profileRepositoryMock).save(Mockito.any());
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(profileRepositoryMock).deleteById(Mockito.any());
        profileServiceMock.deleteById(UUID.randomUUID());
        Mockito.verify(profileRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(profileRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getProfile()),
                            PageRequest.of(0, 5),
                            1));
            profileServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(profileRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            profileServiceMock.getById(UUID.randomUUID());
            Mockito.verify(profileRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getByUsername() {
        try {
            Profile profile = TestData.getProfile();
            profileServiceMock.getByUsername(profile.getUser().getUsername());
            Mockito.verify(profileRepositoryMock).findByUsername(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}