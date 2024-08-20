package org.senla_project.application.mapper;

import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.CollaborationsJoining;

public interface CollaborationsJoiningMapper {

    CollaborationsJoining toEntity(CollaborationsJoiningDto dto);
    CollaborationsJoiningDto toDto(CollaborationsJoining entity);

}
