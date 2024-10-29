package org.senla_project.application.repository;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.*;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        CollaborationsJoiningRepository.class,
        CollaborationRepository.class,
        UserRepository.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class CollaborationsJoiningRepositoryTest {

    final CollaborationsJoiningRepository collabJoiningRepository;
    final CollaborationRepository collabRepository;
    final UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        userRepository.save(TestData.getUser());
        collabRepository.save(TestData.getCollaboration());
    }

    CollaborationsJoining addDependenciesToCollabJoin(CollaborationsJoining collabJoin) {
        collabJoin.setUser(userRepository.findByUsername(collabJoin.getUser().getUsername()).get());
        collabJoin.setCollab(collabRepository.findByCollabName(collabJoin.getCollab().getCollabName()).get());
        return collabJoin;
    }

    @Test
    void create() {
        CollaborationsJoining expectedCollabJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.save(expectedCollabJoining);
        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollabJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollabJoining, actual);
    }

    @Test
    void findById() {
        CollaborationsJoining expectedCollaborationsJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.save(expectedCollaborationsJoining);
        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollaborationsJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    void findAll() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        List<CollaborationsJoining> expectedCollaborationsJoiningList = new ArrayList<>();
        expectedCollaborationsJoiningList.add(collabJoining);
        collabJoiningRepository.save(collabJoining);
        List<CollaborationsJoining> actualCollaborationsJoiningList = collabJoiningRepository.findAll();
        Assertions.assertEquals(expectedCollaborationsJoiningList, actualCollaborationsJoiningList);
    }

    @Test
    void update() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.save(collabJoining);
        CollaborationsJoining expectedCollaborationsJoining = addDependenciesToCollabJoin(TestData.getUpdatedCollabJoining());
        expectedCollaborationsJoining.setJoinId(collabJoining.getJoinId());
        collabJoiningRepository.save(expectedCollaborationsJoining);

        CollaborationsJoining actual = collabJoiningRepository.findById(expectedCollaborationsJoining.getJoinId()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    void deleteById() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getUpdatedCollabJoining());
        collabJoiningRepository.save(collabJoining);
        var collabJoiningId = collabJoining.getJoinId();
        collabJoiningRepository.deleteById(collabJoiningId);
        Optional<CollaborationsJoining> actual = collabJoiningRepository.findById(collabJoiningId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    public void findByUsernameAndCollabName() {
        CollaborationsJoining expectedCollaborationsJoining = addDependenciesToCollabJoin(TestData.getCollabJoining());
        collabJoiningRepository.save(expectedCollaborationsJoining);
        CollaborationsJoining actual = collabJoiningRepository.findByUsernameAndCollabName(expectedCollaborationsJoining.getUser().getUsername(),
                expectedCollaborationsJoining.getCollab().getCollabName()).get();
        Assertions.assertEquals(expectedCollaborationsJoining, actual);
    }

    @Test
    public void deleteByUsernameAndCollabName() {
        CollaborationsJoining collabJoining = addDependenciesToCollabJoin(TestData.getUpdatedCollabJoining());
        collabJoiningRepository.save(collabJoining);
        collabJoiningRepository.deleteByUsernameAndCollabName(collabJoining.getUser().getUsername(), collabJoining.getCollab().getCollabName());
        Assertions.assertTrue(collabJoiningRepository.findAll().isEmpty());
    }
}