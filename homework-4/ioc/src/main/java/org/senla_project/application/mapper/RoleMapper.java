package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.util.Exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Named("RoleMapper")
@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    @Autowired
    RoleDao roleDao;

    @Named("toRoleEntityFromName")
    public Role toRoleEntityFromRoleName(String roleName) {
        return roleDao.findRoleByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    public abstract Role toEntity(RoleDto dto);
    public abstract RoleDto toDto(Role entity);
    public abstract List<Role> toEntityList(List<RoleDto> dtoList);
    public abstract List<RoleDto> toDtoList(List<Role> entityList);

}
