package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new User("Alex", "228", List.of()));
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void create() {
        mockSecurityContext();

        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        Mockito.when(profileMapper.toProfile(profileCreateDto)).thenReturn(TestData.getProfile());
        profileServiceMock.create(profileCreateDto);
        Mockito.verify(profileRepositoryMock).save(Mockito.any());
    }

    @Test
    void update() {
        ProfileUpdateDto profileUpdateDto = TestData.getProfileUpdateDto();
        UUID id = UUID.randomUUID();
        profileUpdateDto.setProfileId(id.toString());

        Mockito.when(profileMapper.toProfile(profileUpdateDto)).thenReturn(TestData.getUpdatedProfile());
        Mockito.when(profileRepositoryMock.findById(id)).thenReturn(Optional.of(TestData.getProfile()));

        profileServiceMock.update(profileUpdateDto);
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