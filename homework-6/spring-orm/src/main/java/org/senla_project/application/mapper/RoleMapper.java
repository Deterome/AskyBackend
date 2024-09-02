package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Named("RoleMapper")
@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    @Autowired
    RoleRepository roleRepository;

    @Named("toRoleEntityFromName")
    public Role toRoleEntityFromRoleName(String roleName) {
        return roleRepository.findRoleByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    public abstract Role toEntity(RoleDto dto);
    public abstract RoleDto toDto(Role entity);
    public abstract List<Role> toEntityList(List<RoleDto> dtoList);
    public abstract List<RoleDto> toDtoList(List<Role> entityList);

}
