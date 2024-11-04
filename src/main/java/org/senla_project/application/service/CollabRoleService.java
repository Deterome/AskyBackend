package org.senla_project.application.service;

import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.collabRole.CollabRoleDeleteDto;
import org.senla_project.application.dto.collabRole.CollabRoleResponseDto;
import org.senla_project.application.dto.collabRole.CollabRoleUpdateDto;

import java.util.List;
import java.util.UUID;

public interface CollabRoleService extends CrudService<CollabRoleCreateDto, CollabRoleResponseDto, CollabRoleUpdateDto, CollabRoleDeleteDto, UUID> {

    CollabRoleResponseDto getByCollabRoleName(String collabRoleName);

    boolean existsByCollabRoleName(String collabRoleName);

    List<CollabRoleResponseDto> getUserRolesInCollab(String username, String collabName);

    boolean hasCollabARole(String collabName, String collabRoleName);

}
