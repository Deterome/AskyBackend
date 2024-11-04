package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningResponseDto;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabDeleteDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.dto.collaboration.CollabUpdateDto;
import org.senla_project.application.util.sort.CollabSortType;

import java.util.UUID;

public interface CollaborationController extends CrudController<CollabCreateDto, CollabResponseDto, CollabUpdateDto, CollabDeleteDto, UUID, CollabSortType> {

    CollabResponseDto getByCollabName(@NonNull String collabName);

    CollaborationsJoiningResponseDto joinToCollab(@NonNull String collabName);

}
