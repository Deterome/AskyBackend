package org.senla_project.application.service;

import org.senla_project.application.dto.collabJoin.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningDeleteDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CollaborationsJoiningService {
    CollaborationsJoiningResponseDto create(CollaborationsJoiningCreateDto element);

    void delete(CollaborationsJoiningDeleteDto collabJoinDeleteDto);

    Page<CollaborationsJoiningResponseDto> getAll(Pageable pageable);

    CollaborationsJoiningResponseDto getById(UUID id);

    CollaborationsJoiningResponseDto getByUsernameAndCollabName(String username, String collabName);

    CollaborationsJoiningResponseDto joinAuthenticatedUserToCollab(String collabName);
}
