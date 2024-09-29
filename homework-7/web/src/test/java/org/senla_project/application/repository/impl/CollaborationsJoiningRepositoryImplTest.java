package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.util.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({DataSourceConfigTest.class, HibernateConfigTest.class, CollaborationsJoiningRepositoryImpl.class})
@Transactional
class CollaborationsJoiningRepositoryImplTest {

    @Autowired
    CollaborationsJoiningRepository collabJoiningRepository;

    @Test
    void create() {
        CollaborationsJoining expectedCollabJoining = TestData.getCollabJoining();
        collabJoiningRepository.create(expectedCollabJoining);
        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollabJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollabJoining, actual);
    }

    @Test
    void findById() {
        CollaborationsJoining expectedCollaborationsJoining = TestData.getCollabJoining();
        collabJoiningRepository.create(expectedCollaborationsJoining);
        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollaborationsJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    void findAll() {
        CollaborationsJoining collabJoining = TestData.getCollabJoining();
        List<CollaborationsJoining> expectedCollaborationsJoiningList = new ArrayList<>();
        expectedCollaborationsJoiningList.add(collabJoining);
        collabJoiningRepository.create(collabJoining);
        List<CollaborationsJoining> actualCollaborationsJoiningList = collabJoiningRepository.findAll();
        Assertions.assertEquals(expectedCollaborationsJoiningList, actualCollaborationsJoiningList);
    }

    @Test
    void update() {
        CollaborationsJoining collabJoining = TestData.getCollabJoining();
        collabJoiningRepository.create(collabJoining);
        CollaborationsJoining expectedCollaborationsJoining = TestData.getUpdatedCollabJoining();
        expectedCollaborationsJoining.setJoinId(collabJoining.getJoinId());
        collabJoiningRepository.update(expectedCollaborationsJoining);

        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollaborationsJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    void deleteById() {
        CollaborationsJoining collabJoining = TestData.getUpdatedCollabJoining();
        collabJoiningRepository.create(collabJoining);
        var collabJoiningId = collabJoining.getJoinId();
        collabJoiningRepository.deleteById(collabJoiningId);
        Optional<CollaborationsJoining> actual = collabJoiningRepository.findById(collabJoiningId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findCollabJoin() {
        CollaborationsJoining expectedCollaborationsJoining = TestData.getCollabJoining();
        collabJoiningRepository.create(expectedCollaborationsJoining);
        CollaborationsJoining actual = collabJoiningRepository.findCollabJoin(expectedCollaborationsJoining.getUser().getNickname(),
                expectedCollaborationsJoining.getCollab().getCollabName()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }
}