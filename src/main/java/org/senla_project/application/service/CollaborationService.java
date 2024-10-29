package org.senla_project.application.service;

import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabDeleteDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;

import java.util.UUID;

public interface CollaborationService extends CrudService<CollabCreateDto, CollabResponseDto, CollabDeleteDto, UUID> {
    CollabResponseDto getByCollabName(String collabName);
    boolean isUserACreatorOfCollab(String username, String collabName);
}
