package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.dto.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CollaborationsJoiningServiceTest {

    @Mock
    CollaborationsJoiningRepository collabJoinRepositoryMock;
    @Mock
    CollaborationsJoiningMapper collabJoinMapperMock;
    @Spy
    UserMapper userMapperSpy;
    @Mock
    UserService userServiceMock;
    @Spy
    CollaborationMapper collabMapperSpy;
    @Mock
    CollaborationService collabServiceMock;
    @InjectMocks
    CollaborationsJoiningService collabJoinServiceMock;

    @Test
    void create() {
        CollaborationsJoiningCreateDto collabJoinCreateDto = TestData.getCollabJoiningCreateDto();
        Mockito.when(collabJoinMapperMock.toCollabJoin(collabJoinCreateDto)).thenReturn(TestData.getCollabJoining());
        Mockito.when(userMapperSpy.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(collabMapperSpy.toCollab((CollaborationResponseDto) Mockito.any())).thenReturn(TestData.getCollaboration());
        collabJoinServiceMock.create(collabJoinCreateDto);
        Mockito.verify(collabJoinRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        CollaborationsJoiningCreateDto collabJoinCreateDto = TestData.getCollabJoiningCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(collabJoinMapperMock.toCollabJoin(id, collabJoinCreateDto)).thenReturn(TestData.getCollabJoining());
        Mockito.when(userMapperSpy.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(collabMapperSpy.toCollab((CollaborationResponseDto) Mockito.any())).thenReturn(TestData.getCollaboration());
        Mockito.when(collabJoinRepositoryMock.existsById(id)).thenReturn(true);
        collabJoinServiceMock.updateById(id, collabJoinCreateDto);
        Mockito.verify(collabJoinRepositoryMock).save(Mockito.any());
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(collabJoinRepositoryMock).deleteById(Mockito.any());
        collabJoinServiceMock.deleteById(UUID.randomUUID());
        Mockito.verify(collabJoinRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(collabJoinRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getCollabJoining()),
                            PageRequest.of(0, 5),
                            1));
            collabJoinServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(collabJoinRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            collabJoinServiceMock.getById(UUID.randomUUID());
            Mockito.verify(collabJoinRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findCollaborationsJoining() {
        try {
            CollaborationsJoining collabJoin = TestData.getCollabJoining();
            collabJoinServiceMock.getByParams(collabJoin.getUser().getUsername(), collabJoin.getCollab().getCollabName());
            Mockito.verify(collabJoinRepositoryMock).findByUsernameAndCollabName(Mockito.any(), Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}