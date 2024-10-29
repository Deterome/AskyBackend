package org.senla_project.application.service;

import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleResponseDto;

import java.util.UUID;

public interface RoleService extends CrudService<RoleCreateDto, RoleResponseDto, RoleDeleteDto, UUID> {
    RoleResponseDto getByRoleName(String roleName);
    boolean existByRoleName(String roleName);
}
