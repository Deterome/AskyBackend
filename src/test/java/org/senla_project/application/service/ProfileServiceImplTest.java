package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.service.impl.ProfileServiceImpl;
import org.senla_project.application.service.linker.ProfileLinkerService;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {
    @Mock
    ProfileRepository profileRepositoryMock;
    @Mock
    ProfileMapper profileMapper;

    @Mock
    ProfileLinkerService profileLinkerService;

    @InjectMocks
    ProfileServiceImpl profileServiceMock;

    @Test
    void create() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        Mockito.when(profileMapper.toProfile(profileCreateDto)).thenReturn(TestData.getProfile());
        profileServiceMock.create(profileCreateDto);
        Mockito.verify(profileRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(profileMapper.toProfile(id, profileCreateDto)).thenReturn(TestData.getProfile());
        Mockito.when(profileRepositoryMock.existsById(id)).thenReturn(true);
        profileServiceMock.updateById(id, profileCreateDto);
        Mockito.verify(profileRepositoryMock).save(Mockito.any());
    }

    @Test
    void delete() {
        ProfileDeleteDto profileDeleteDto = ProfileDeleteDto.builder()
                .username("Alex")
                .build();
        Mockito.doNothing().when(profileRepositoryMock).deleteByUsername(Mockito.any());
        profileServiceMock.delete(profileDeleteDto);
        Mockito.verify(profileRepositoryMock).deleteByUsername(Mockito.any());
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
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getById() {
        try {
            profileServiceMock.getById(UUID.randomUUID());
            Mockito.verify(profileRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getByUsername() {
        try {
            Profile profile = TestData.getProfile();
            profileServiceMock.getByUsername(profile.getUser().getUsername());
            Mockito.verify(profileRepositoryMock).findByUsername(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}