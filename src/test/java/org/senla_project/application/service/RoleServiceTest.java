package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    RoleRepository roleRepositoryMock;
    @Spy
    RoleMapper roleMapperSpy;
    @InjectMocks
    RoleService roleServiceMock;

    @Test
    void addElement() {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleServiceMock.addElement(roleCreateDto);
        Mockito.verify(roleRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleServiceMock.updateElement(UUID.randomUUID(), roleCreateDto);
        Mockito.verify(roleRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(roleRepositoryMock).deleteById(Mockito.any());
        roleServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(roleRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void findAllElements() {
        try {
            roleServiceMock.findAllElements(1);
            Mockito.verify(roleRepositoryMock).findAll(1);
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void findElementById() {
        try {
            roleServiceMock.findElementById(UUID.randomUUID());
            Mockito.verify(roleRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void findRoleByName() {
        try {
            Role role = TestData.getRole();
            roleServiceMock.findRoleByName(role.getRoleName());
            Mockito.verify(roleRepositoryMock).findRoleByName(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}