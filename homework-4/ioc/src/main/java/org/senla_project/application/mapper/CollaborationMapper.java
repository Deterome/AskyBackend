package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.entity.Collaboration;

@Mapper(componentModel = "spring")
public interface CollaborationMapper {

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    Collaboration toEntity(CollaborationDto dto);

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    CollaborationDto toDto(Collaboration entity);

}
