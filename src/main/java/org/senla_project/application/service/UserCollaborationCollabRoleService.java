package org.senla_project.application.service;

import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleCreateDto;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleDeleteDto;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleResponseDto;
import org.senla_project.application.entity.identifiers.UserCollaborationCollabRoleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCollaborationCollabRoleService {
    UserCollaborationCollabRoleResponseDto create(UserCollaborationCollabRoleCreateDto element);

    void delete(UserCollaborationCollabRoleDeleteDto collabJoinDeleteDto);

    Page<UserCollaborationCollabRoleResponseDto> getAll(Pageable pageable);

    UserCollaborationCollabRoleResponseDto getById(UserCollaborationCollabRoleId id);
    
    void giveUserARoleInCollab(String username, String collabName, String collabRole);
}
