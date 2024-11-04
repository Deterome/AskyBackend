package org.senla_project.application.service;

import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleResponseDto;
import org.senla_project.application.dto.role.RoleUpdateDto;

import java.util.UUID;

public interface RoleService extends CrudService<RoleCreateDto, RoleResponseDto, RoleUpdateDto, RoleDeleteDto, UUID> {

    RoleResponseDto getByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

}
