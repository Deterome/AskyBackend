package org.senla_project.application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.senla_project.application.entity.identifiers.UserCollaborationCollabRoleId;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        UserRepository.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class UserCollaborationCollabRoleRepositoryTest {

    private final UserCollaborationCollabRoleRepository userCollabRoleRepository;
    private final UserRepository userRepository;
    private final CollaborationRepository collabRepository;
    private final CollabRoleRepository collabRoleRepository;

    @BeforeEach
    void initDataBaseWithData() {
        userRepository.save(TestData.getUser());
        collabRepository.save(TestData.getCollaboration());
        collabRoleRepository.save(TestData.getCollabRole());
    }

    UserCollaborationCollabRole addDependenciesToUserCollabRole(UserCollaborationCollabRole userCollabRole) {
        userCollabRole.setUser(userRepository.findByUsername(userCollabRole.getUser().getUsername()).get());
        userCollabRole.setCollab(collabRepository.findByCollabName(userCollabRole.getCollab().getCollabName()).get());
        userCollabRole.setCollabRole(collabRoleRepository.findByCollabRoleName(userCollabRole.getCollabRole().getCollabRoleName()).get());
        return userCollabRole;
    }

    @Test
    void create() {
        UserCollaborationCollabRole expectedUserCollabRole = TestData.getUserCollaborationCollabRole();
        userCollabRoleRepository.save(addDependenciesToUserCollabRole(expectedUserCollabRole));
        UserCollaborationCollabRole actual = userCollabRoleRepository.findById(UserCollaborationCollabRoleId.builder()
                .user(expectedUserCollabRole.getUser().getUserId())
                .collab(expectedUserCollabRole.getCollab().getCollabId())
                .collabRole(expectedUserCollabRole.getCollabRole().getCollabRoleId())
                .build()).get();
        Assertions.assertEquals(expectedUserCollabRole, actual);
    }

    @Test
    void findById() {
        UserCollaborationCollabRole expectedUserCollabRole = TestData.getUserCollaborationCollabRole();
        userCollabRoleRepository.save(addDependenciesToUserCollabRole(expectedUserCollabRole));
        UserCollaborationCollabRole actual = userCollabRoleRepository.findById(UserCollaborationCollabRoleId.builder()
                .user(expectedUserCollabRole.getUser().getUserId())
                .collab(expectedUserCollabRole.getCollab().getCollabId())
                .collabRole(expectedUserCollabRole.getCollabRole().getCollabRoleId())
                .build()).get();
        Assertions.assertEquals(expectedUserCollabRole, actual);
    }

    @Test
    void findAll() {
        UserCollaborationCollabRole userCollabRole = addDependenciesToUserCollabRole(TestData.getUserCollaborationCollabRole());
        userCollabRoleRepository.save(userCollabRole);
        List<UserCollaborationCollabRole> expectedUserList = new ArrayList<>();
        expectedUserList.add(userCollabRole);
        List<UserCollaborationCollabRole> actualUserList = userCollabRoleRepository.findAll();
        Assertions.assertEquals(expectedUserList, actualUserList);
    }

    @Test
    void update() {
        collabRoleRepository.save(TestData.getUpdatedCollabRole());
        UserCollaborationCollabRole userCollabRole = addDependenciesToUserCollabRole(TestData.getUserCollaborationCollabRole());
        userCollabRoleRepository.save(userCollabRole);
        UserCollaborationCollabRole updatedUserCollabRole = addDependenciesToUserCollabRole(TestData.getUpdatedUserCollaborationCollabRole());
        userCollabRoleRepository.save(updatedUserCollabRole);

        UserCollaborationCollabRole actual = userCollabRoleRepository.findById(UserCollaborationCollabRoleId.builder()
                .user(userCollabRole.getUser().getUserId())
                .collab(userCollabRole.getCollab().getCollabId())
                .collabRole(userCollabRole.getCollabRole().getCollabRoleId())
                .build()).get();
        Assertions.assertEquals(updatedUserCollabRole, actual);
    }

    @Test
    void deleteById() {
        collabRoleRepository.save(TestData.getUpdatedCollabRole());
        UserCollaborationCollabRole userCollabRole = addDependenciesToUserCollabRole(TestData.getUserCollaborationCollabRole());
        userCollabRoleRepository.save(userCollabRole);
        var id = UserCollaborationCollabRoleId.builder()
                .user(userCollabRole.getUser().getUserId())
                .collab(userCollabRole.getCollab().getCollabId())
                .collabRole(userCollabRole.getCollabRole().getCollabRoleId())
                .build();
        userCollabRoleRepository.deleteById(id);
        Optional<UserCollaborationCollabRole> actual = userCollabRoleRepository.findById(id);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void deleteByUsernameAndCollabNameAndCollabRoleName() {
        collabRoleRepository.save(TestData.getUpdatedCollabRole());
        UserCollaborationCollabRole userCollabRole = addDependenciesToUserCollabRole(TestData.getUserCollaborationCollabRole());
        userCollabRoleRepository.save(userCollabRole);
        userCollabRoleRepository.deleteByUsernameAndCollabNameAndCollabRoleName(
                userCollabRole.getUser().getUsername(),
                userCollabRole.getCollab().getCollabName(),
                userCollabRole.getCollabRole().getCollabRoleName());
        Assertions.assertTrue(userCollabRoleRepository.findAll().isEmpty());
    }

}