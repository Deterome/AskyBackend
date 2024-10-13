package org.senla_project.application.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.repository.UserRepository;
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
        CollaborationsJoiningRepositoryImpl.class,
        CollaborationRepositoryImpl.class,
        UserRepositoryImpl.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class CollaborationsJoiningRepositoryImplTest {

    final CollaborationsJoiningRepository collabJoiningRepository;
    final CollaborationRepository collabRepository;
    final UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        userRepository.create(TestData.getUser());
        collabRepository.create(TestData.getCollaboration());
    }

    CollaborationsJoining addDependenciesToCollabJoin(CollaborationsJoining collabJoin) {
        collabJoin.setUser(userRepository.findUserByUsername(collabJoin.getUser().getUsername()).get());
        collabJoin.setCollab(collabRepository.findCollabByName(collabJoin.getCollab().getCollabName()).get());
        return collabJoin;
    }

    @Test
    void create() {
        CollaborationsJoining expectedCollabJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.create(expectedCollabJoining);
        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollabJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollabJoining, actual);
    }

    @Test
    void findById() {
        CollaborationsJoining expectedCollaborationsJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.create(expectedCollaborationsJoining);
        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollaborationsJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    void findAll() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        List<CollaborationsJoining> expectedCollaborationsJoiningList = new ArrayList<>();
        expectedCollaborationsJoiningList.add(collabJoining);
        collabJoiningRepository.create(collabJoining);
        List<CollaborationsJoining> actualCollaborationsJoiningList = collabJoiningRepository.findAll();
        Assertions.assertEquals(expectedCollaborationsJoiningList, actualCollaborationsJoiningList);
    }

    @Test
    void update() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.create(collabJoining);
        CollaborationsJoining expectedCollaborationsJoining = addDependenciesToCollabJoin(TestData.getUpdatedCollabJoining());
        expectedCollaborationsJoining.setJoinId(collabJoining.getJoinId());
        collabJoiningRepository.update(expectedCollaborationsJoining);

        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollaborationsJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    void deleteById() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getUpdatedCollabJoining());
        collabJoiningRepository.create(collabJoining);
        var collabJoiningId = collabJoining.getJoinId();
        collabJoiningRepository.deleteById(collabJoiningId);
        Optional<CollaborationsJoining> actual = collabJoiningRepository.findById(collabJoiningId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findCollabJoin() {
        CollaborationsJoining expectedCollaborationsJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.create(expectedCollaborationsJoining);
        CollaborationsJoining actual = collabJoiningRepository.findCollabJoin(expectedCollaborationsJoining.getUser().getUsername(),
                expectedCollaborationsJoining.getCollab().getCollabName()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }
}