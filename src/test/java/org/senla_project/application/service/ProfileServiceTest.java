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
    void addElement() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        Mockito.when(profileMapper.toProfile(profileCreateDto)).thenReturn(TestData.getProfile());
        Mockito.when(userMapper.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        profileServiceMock.addElement(profileCreateDto);
        Mockito.verify(profileRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(profileMapper.toProfile(id, profileCreateDto)).thenReturn(TestData.getProfile());
        Mockito.when(userMapper.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        profileServiceMock.updateElement(id, profileCreateDto);
        Mockito.verify(profileRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(profileRepositoryMock).deleteById(Mockito.any());
        profileServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(profileRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void findAllElements() {
        try {
            profileServiceMock.findAllElements(1);
            Mockito.verify(profileRepositoryMock).findAll(1);
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findElementById() {
        try {
            profileServiceMock.findElementById(UUID.randomUUID());
            Mockito.verify(profileRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findProfileByUsername() {
        try {
            Profile profile = TestData.getProfile();
            profileServiceMock.findProfileByUsername(profile.getUser().getUsername());
            Mockito.verify(profileRepositoryMock).findProfileByUsername(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}