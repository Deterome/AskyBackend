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
    void addElement() {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabServiceMock.addElement(collabCreateDto);
        Mockito.verify(collabRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabServiceMock.updateElement(UUID.randomUUID(), collabCreateDto);
        Mockito.verify(collabRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(collabRepositoryMock).deleteById(Mockito.any());
        collabServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(collabRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void findAllElements() {
        try {
            collabServiceMock.findAllElements();
            Mockito.verify(collabRepositoryMock).findAll();
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findElementById() {
        try {
            collabServiceMock.findElementById(UUID.randomUUID());
            Mockito.verify(collabRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findCollaborationByName() {
        try {
            Collaboration collab = TestData.getCollaboration();
            collabServiceMock.findCollabByName(collab.getCollabName());
            Mockito.verify(collabRepositoryMock).findCollabByName(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}