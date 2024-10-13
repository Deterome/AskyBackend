package org.senla_project.application.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        CollaborationRepositoryImpl.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class CollaborationRepositoryImplTest {

    final CollaborationRepository collaborationRepository;

    @Test
    void create() {
        Collaboration expectedCollaboration = TestData.getCollaboration();
        collaborationRepository.create(expectedCollaboration);
        Collaboration actual = collaborationRepository.findById(expectedCollaboration.getCollabId()).get();
        Assertions.assertEquals(expectedCollaboration, actual);
    }

    @Test
    void findById() {
        Collaboration expectedCollaboration = TestData.getCollaboration();
        collaborationRepository.create(expectedCollaboration);
        Collaboration actual = collaborationRepository.findById(expectedCollaboration.getCollabId()).get();
        Assertions.assertEquals(expectedCollaboration, actual);
    }

    @Test
    void findAll() {
        Collaboration collaboration = TestData.getCollaboration();
        List<Collaboration> expectedCollaborationList = new ArrayList<>();
        expectedCollaborationList.add(collaboration);
        collaborationRepository.create(collaboration);
        List<Collaboration> actualCollaborationList = collaborationRepository.findAll();
        Assertions.assertEquals(expectedCollaborationList, actualCollaborationList);
    }

    @Test
    void update() {
        Collaboration collaboration = TestData.getCollaboration();
        collaborationRepository.create(collaboration);
        Collaboration expectedCollaboration = TestData.getUpdatedCollaboration();
        expectedCollaboration.setCollabId(collaboration.getCollabId());
        collaborationRepository.update(expectedCollaboration);

        Collaboration actual = collaborationRepository.findById(expectedCollaboration.getCollabId()).get();
        Assertions.assertEquals(expectedCollaboration, actual);
    }

    @Test
    void deleteById() {
        Collaboration collaboration = TestData.getCollaboration();
        collaborationRepository.create(collaboration);
        var collaborationId = collaboration.getCollabId();
        collaborationRepository.deleteById(collaborationId);
        Optional<Collaboration> actual = collaborationRepository.findById(collaborationId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findCollabByName() {
        Collaboration expectedCollaboration = TestData.getCollaboration();
        collaborationRepository.create(expectedCollaboration);
        Collaboration actual = collaborationRepository.findCollabByName(expectedCollaboration.getCollabName()).get();
        Assertions.assertEquals(expectedCollaboration, actual);
    }
}