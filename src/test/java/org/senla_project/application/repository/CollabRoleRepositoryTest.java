package org.senla_project.application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.UserCollaborationCollabRole;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        CollabRoleRepository.class,
        UserRepository.class,
        CollaborationRepository.class,
        UserCollaborationCollabRoleRepository.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class CollabRoleRepositoryTest {

    final CollabRoleRepository collabRoleRepository;
    final CollaborationRepository collabRepository;
    final UserRepository userRepository;
    final UserCollaborationCollabRoleRepository userCollabRoleRepository;

    @Test
    void create() {
        CollabRole expectedRole = TestData.getCollabRole();
        collabRoleRepository.save(expectedRole);
        CollabRole actual = collabRoleRepository.findById(expectedRole.getCollabRoleId()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    void findById() {
        CollabRole expectedRole = TestData.getCollabRole();
        collabRoleRepository.save(expectedRole);
        CollabRole actual = collabRoleRepository.findById(expectedRole.getCollabRoleId()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    void findAll() {
        CollabRole collabRole = TestData.getCollabRole();
        List<CollabRole> expectedRoleList = new ArrayList<>();
        expectedRoleList.add(collabRole);
        collabRoleRepository.save(collabRole);
        List<CollabRole> actualRoleList = collabRoleRepository.findAll();
        Assertions.assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    void update() {
        CollabRole collabRole = TestData.getCollabRole();
        collabRoleRepository.save(collabRole);
        CollabRole expectedRole = TestData.getUpdatedCollabRole();
        expectedRole.setCollabRoleId(collabRole.getCollabRoleId());
        collabRoleRepository.save(expectedRole);

        CollabRole actual = collabRoleRepository.findByCollabRoleName(expectedRole.getCollabRoleName()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    void deleteById() {
        CollabRole collabRole = TestData.getCollabRole();
        collabRoleRepository.save(collabRole);
        var roleId = collabRole.getCollabRoleId();
        collabRoleRepository.deleteById(roleId);
        Optional<CollabRole> actual = collabRoleRepository.findById(roleId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByCollabRoleName() {
        CollabRole expectedRole = TestData.getCollabRole();
        collabRoleRepository.save(expectedRole);
        CollabRole actual = collabRoleRepository.findByCollabRoleName(expectedRole.getCollabRoleName()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    public void findByUsernameAndCollabName() {
        User user = TestData.getUser();
        Collaboration collab = TestData.getCollaboration();
        CollabRole collabRole = TestData.getCollabRole();
        UserCollaborationCollabRole userCollabRole = UserCollaborationCollabRole.builder()
                .user(user)
                .collab(collab)
                .collabRole(collabRole)
                .build();

        userRepository.save(user);
        collabRoleRepository.save(collabRole);
        collabRepository.save(collab);
        userCollabRoleRepository.save(userCollabRole);

        List<CollabRole> expectedCollabRoles = List.of(collabRole);
        List<CollabRole> actualCollabRoles = collabRoleRepository.findByUsernameAndCollabName(user.getUsername(), collab.getCollabName());

        Assertions.assertEquals(expectedCollabRoles, actualCollabRoles);
    }

    @Test
    public void findByCollabName() {
        CollabRole collabRole = TestData.getCollabRole();
        Collaboration collab = TestData.getCollaboration();

        collab.setCollabRoles(Set.of(collabRole));

        collabRoleRepository.save(collabRole);
        collabRepository.save(collab);

        List<CollabRole> actualCollabRoleList = collabRoleRepository.findByCollabName(collab.getCollabName());
        List<CollabRole> expectedCollabRoleList = List.of(collabRole);

        Assertions.assertEquals(actualCollabRoleList, expectedCollabRoleList);
    }

}