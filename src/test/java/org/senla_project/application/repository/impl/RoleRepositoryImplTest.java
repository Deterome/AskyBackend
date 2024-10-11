package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Role;
import org.senla_project.application.repository.RoleRepository;
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
        RoleRepositoryImpl.class
})
@Transactional
class RoleRepositoryImplTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    void create() {
        Role expectedRole = TestData.getRole();
        roleRepository.create(expectedRole);
        Role actual = roleRepository.findById(expectedRole.getRoleId()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    void findById() {
        Role expectedRole = TestData.getRole();
        roleRepository.create(expectedRole);
        Role actual = roleRepository.findById(expectedRole.getRoleId()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    void findAll() {
        Role role = TestData.getRole();
        List<Role> expectedRoleList = new ArrayList<>();
        expectedRoleList.add(role);
        roleRepository.create(role);
        List<Role> actualRoleList = roleRepository.findAll();
        Assertions.assertEquals(expectedRoleList, actualRoleList);
    }

    @Test
    void update() {
        Role role = TestData.getRole();
        roleRepository.create(role);
        Role expectedRole = TestData.getUpdatedRole();
        expectedRole.setRoleId(role.getRoleId());
        roleRepository.update(expectedRole);

        Role actual = roleRepository.findRoleByName(expectedRole.getRoleName()).get();
        Assertions.assertEquals(expectedRole, actual);
    }

    @Test
    void deleteById() {
        Role role = TestData.getRole();
        roleRepository.create(role);
        var roleId = role.getRoleId();
        roleRepository.deleteById(roleId);
        Optional<Role> actual = roleRepository.findById(roleId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findRoleByName() {
        Role expectedRole = TestData.getRole();
        roleRepository.create(expectedRole);
        Role actual = roleRepository.findRoleByName(expectedRole.getRoleName()).get();
        Assertions.assertEquals(expectedRole, actual);
    }
}