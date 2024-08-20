package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.Role;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleDto dto);
    RoleDto toDto(Role entity);

}
