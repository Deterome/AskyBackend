package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.entity.Collaboration;

import java.util.List;

@Mapper(componentModel = "spring", uses = CollaborationMapper.class)
public interface CollaborationListMapper {

    List<Collaboration> toEntityList(List<CollaborationDto> dtoList);
    List<CollaborationDto> toDtoList(List<Collaboration> entityList);

}


