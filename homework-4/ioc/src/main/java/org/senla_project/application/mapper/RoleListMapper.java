package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.entity.Role;

import java.util.List;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface RoleListMapper {

    List<Role> toEntityList(List<RoleDto> dtoList);
    List<RoleDto> toDtoList(List<Role> entityList);

}


