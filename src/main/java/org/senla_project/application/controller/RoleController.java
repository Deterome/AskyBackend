package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleResponseDto;
import org.senla_project.application.dto.role.RoleUpdateDto;

import java.util.UUID;

public interface RoleController extends CrudController<RoleCreateDto, RoleResponseDto, RoleUpdateDto, RoleDeleteDto, UUID> {

    RoleResponseDto getByRoleName(@NonNull String roleName);

}
