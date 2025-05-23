package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleUpdateDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.service.impl.RoleServiceImpl;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepositoryMock;
    @Spy
    RoleMapper roleMapperSpy;

    @InjectMocks
    RoleServiceImpl roleServiceMock;

    @Test
    void create() {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleServiceMock.create(roleCreateDto);
        Mockito.verify(roleRepositoryMock).save(Mockito.any());
    }

    @Test
    void update() {
        RoleUpdateDto roleUpdateDto = TestData.getRoleUpdateDto();
        UUID id = UUID.randomUUID();
        roleUpdateDto.setRoleId(id.toString());

        Mockito.when(roleRepositoryMock.findById(id)).thenReturn(Optional.of(TestData.getRole()));

        roleServiceMock.update(roleUpdateDto);

        Mockito.verify(roleRepositoryMock).save(Mockito.any());
    }

    @Test
    void delete() {
        RoleDeleteDto roleDeleteDto = RoleDeleteDto.builder()
                .roleName("god")
                .build();
        Mockito.doNothing().when(roleRepositoryMock).deleteByRoleName(Mockito.any());
        roleServiceMock.delete(roleDeleteDto);
        Mockito.verify(roleRepositoryMock).deleteByRoleName(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(roleRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getRole()),
                            PageRequest.of(0, 5),
                            1));
            roleServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(roleRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getById() {
        try {
            roleServiceMock.getById(UUID.randomUUID());
            Mockito.verify(roleRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }

    @Test
    void getByRoleName() {
        try {
            Role role = TestData.getRole();
            roleServiceMock.getByRoleName(role.getRoleName());
            Mockito.verify(roleRepositoryMock).findByRoleName(Mockito.any());
        } catch (EntityNotFoundException ignored) {
        }
    }
}