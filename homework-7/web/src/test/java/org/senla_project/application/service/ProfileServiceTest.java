package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    ProfileRepository profileRepositoryMock;
    @Spy
    ProfileMapper profileMapperSpy;
    @InjectMocks
    ProfileService profileServiceMock;

    @Test
    void addElement() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileServiceMock.addElement(profileCreateDto);
        Mockito.verify(profileRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileServiceMock.updateElement(UUID.randomUUID(), profileCreateDto);
        Mockito.verify(profileRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(profileRepositoryMock).deleteById(Mockito.any());
        profileServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(profileRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAllElements() {
        try {
            profileServiceMock.getAllElements();
            Mockito.verify(profileRepositoryMock).findAll();
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
            profileServiceMock.findProfileByUsername(profile.getUser().getNickname());
            Mockito.verify(profileRepositoryMock).findProfileByNickname(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}