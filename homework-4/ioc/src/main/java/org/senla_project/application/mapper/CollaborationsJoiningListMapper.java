package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.CollaborationsJoining;

import java.util.List;

@Mapper(componentModel = "spring", uses = CollaborationsJoiningMapper.class)
public interface CollaborationsJoiningListMapper {

    List<CollaborationsJoining> toEntityList(List<CollaborationsJoiningDto> dtoList);
    List<CollaborationsJoiningDto> toDtoList(List<CollaborationsJoining> entityList);

}


