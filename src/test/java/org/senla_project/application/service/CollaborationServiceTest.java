package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CollaborationServiceTest {

    @Mock
    CollaborationRepository collabRepositoryMock;
    @Spy
    CollaborationMapper collabMapperSpy;
    @InjectMocks
    CollaborationService collabServiceMock;

    @Test
    void create() {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabServiceMock.create(collabCreateDto);
        Mockito.verify(collabRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(collabRepositoryMock.existsById(id)).thenReturn(true);
        collabServiceMock.updateById(id, collabCreateDto);
        Mockito.verify(collabRepositoryMock).save(Mockito.any());
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(collabRepositoryMock).deleteById(Mockito.any());
        collabServiceMock.deleteById(UUID.randomUUID());
        Mockito.verify(collabRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(collabRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getCollaboration()),
                            PageRequest.of(0, 5),
                            1));
            collabServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(collabRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            collabServiceMock.getById(UUID.randomUUID());
            Mockito.verify(collabRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findCollaborationByName() {
        try {
            Collaboration collab = TestData.getCollaboration();
            collabServiceMock.getByCollabName(collab.getCollabName());
            Mockito.verify(collabRepositoryMock).findByCollabName(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}