package org.senla_project.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.collabRole.CollabRoleResponseDto;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabDeleteDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.mapper.CollabRoleMapper;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.service.impl.CollaborationServiceImpl;
import org.senla_project.application.service.linker.CollaborationLinkerService;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.enums.DefaultCollabRole;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollaborationServiceImplTest {
    @Mock
    CollaborationRepository collabRepositoryMock;
    @Spy
    CollaborationMapper collabMapperMock;

    @Spy
    CollabRoleMapper collabRoleMapperSpy;
    @Mock
    CollabRoleService collabRoleServiceMock;

    @Mock
    UserCollaborationCollabRoleService userCollabRoleServiceMock;

    @Mock
    CollaborationsJoiningService collabJoinServiceMock;

    @Mock
    CollaborationLinkerService collabLinkerService;

    @InjectMocks
    CollaborationServiceImpl collabServiceMock;

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

        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();

        when(collabMapperMock.toCollab(collabCreateDto)).thenReturn(TestData.getCollaboration());
        when(collabMapperMock.toCollabResponseDto(any())).thenReturn(CollabResponseDto.builder().collabName("Bros").build());
        doNothing().when(userCollabRoleServiceMock).giveUserARoleInCollab(any(), any(), any());
        collabServiceMock.create(collabCreateDto);
        verify(collabRepositoryMock).save(any());
    }

    @Test
    void isUserACreatorOfCollab() {
        List<CollabRoleResponseDto> collabRoleResponse = List.of(CollabRoleResponseDto.builder()
                .collabRoleName(DefaultCollabRole.CREATOR.toString())
                .build());

        when(collabRoleServiceMock.getUserRolesInCollab(anyString(), anyString()))
                .thenReturn(collabRoleResponse);

        boolean result = collabServiceMock.isUserACreatorOfCollab("Alex", "228");

        Assertions.assertTrue(result);
    }

    @Test
    void updateById() {
        mockSecurityContext();

        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        UUID id = UUID.randomUUID();

        List<CollabRoleResponseDto> collabRoleResponse = List.of(CollabRoleResponseDto.builder()
                .collabRoleName(DefaultCollabRole.CREATOR.toString())
                .build());

        when(collabRepositoryMock.findById(id)).thenReturn(Optional.of(TestData.getCollaboration()));
        when(collabRoleServiceMock.getUserRolesInCollab(anyString(), anyString())).thenReturn(collabRoleResponse);

        collabServiceMock.updateById(id, collabCreateDto);

        verify(collabRepositoryMock).save(any());
    }

    @Test
    void delete() {
        mockSecurityContext();

        CollabDeleteDto collabDeleteDto = CollabDeleteDto.builder()
                .collabName("Bros")
                .build();
        doNothing().when(collabRepositoryMock).deleteByCollabName(any());
        List<CollabRoleResponseDto> collabRoleResponse = List.of(CollabRoleResponseDto.builder()
                .collabRoleName(DefaultCollabRole.CREATOR.toString())
                .build());
        when(collabRoleServiceMock.getUserRolesInCollab(anyString(), anyString()))
                .thenReturn(collabRoleResponse);

        collabServiceMock.delete(collabDeleteDto);

        verify(collabRepositoryMock).deleteByCollabName(any());
    }

    @Test
    void getAll() {
        try {
            when(collabRepositoryMock.findAll((Pageable) any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getCollaboration()),
                            PageRequest.of(0, 5),
                            1));
            collabServiceMock.getAll(PageRequest.of(0, 5));
            verify(collabRepositoryMock).findAll((Pageable) any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getById() {
        try {
            collabServiceMock.getById(UUID.randomUUID());
            verify(collabRepositoryMock).findById(any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void findCollaborationByName() {
        try {
            Collaboration collab = TestData.getCollaboration();
            collabServiceMock.getByCollabName(collab.getCollabName());
            verify(collabRepositoryMock).findByCollabName(any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}