package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningDeleteDto;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollabRoleMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.service.impl.CollaborationsJoiningServiceImpl;
import org.senla_project.application.service.linker.CollaborationsJoiningLinkerService;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollaborationsJoiningServiceImplTest {
    @Mock
    CollaborationsJoiningRepository collabJoinRepositoryMock;
    @Mock
    CollaborationsJoiningMapper collabJoinMapperMock;

    @Spy
    CollabRoleMapper collabRoleMapperSpy;

    @Mock
    UserCollaborationCollabRoleService userCollabRoleMock;

    @Mock
    CollaborationsJoiningLinkerService collabJoinLinkerService;

    @InjectMocks
    CollaborationsJoiningServiceImpl collabJoinServiceMock;

    @Test
    void create() {
        CollaborationsJoiningCreateDto collabJoinCreateDto = TestData.getCollabJoiningCreateDto();
        when(collabJoinMapperMock.toCollabJoin(collabJoinCreateDto)).thenReturn(TestData.getCollabJoining());
        collabJoinServiceMock.create(collabJoinCreateDto);
        verify(collabJoinRepositoryMock).save(any());
    }

    @Test
    void delete() {
        CollaborationsJoiningDeleteDto collaborationsJoiningDeleteDto = CollaborationsJoiningDeleteDto.builder()
                .collabName("Bros")
                .username("Alex")
                .build();
        doNothing().when(collabJoinRepositoryMock).deleteByUsernameAndCollabName(any(), any());
        collabJoinServiceMock.delete(collaborationsJoiningDeleteDto);
        verify(collabJoinRepositoryMock).deleteByUsernameAndCollabName(any(), any());
    }

    @Test
    void getAll() {
        try {
            when(collabJoinRepositoryMock.findAll((Pageable) any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getCollabJoining()),
                            PageRequest.of(0, 5),
                            1));
            collabJoinServiceMock.getAll(PageRequest.of(0, 5));
            verify(collabJoinRepositoryMock).findAll((Pageable) any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            collabJoinServiceMock.getById(UUID.randomUUID());
            verify(collabJoinRepositoryMock).findById(any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findCollaborationsJoining() {
        try {
            CollaborationsJoining collabJoin = TestData.getCollabJoining();
            collabJoinServiceMock.getByUsernameAndCollabName(collabJoin.getUser().getUsername(), collabJoin.getCollab().getCollabName());
            verify(collabJoinRepositoryMock).findByUsernameAndCollabName(any(), any());
        } catch (EntityNotFoundException ignored) {}
    }
}