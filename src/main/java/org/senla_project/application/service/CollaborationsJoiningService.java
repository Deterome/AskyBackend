package org.senla_project.application.service;

import org.senla_project.application.dto.collabJoin.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningDeleteDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningResponseDto;

import java.util.UUID;

public interface CollaborationsJoiningService extends CrudService<CollaborationsJoiningCreateDto, CollaborationsJoiningResponseDto, CollaborationsJoiningDeleteDto, UUID> {
    CollaborationsJoiningResponseDto getByUsernameAndCollabName(String username, String collabName);
    CollaborationsJoiningResponseDto joinAuthenticatedUserToCollab(String collabName);
}
