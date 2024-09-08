package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.entity.Role;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Named("RoleMapper")
@Mapper(componentModel = "spring", uses = {UuidMapper.class})
public abstract class RoleMapper {

    @Autowired
    RoleRepository roleRepository;

    @Named("toRoleEntityFromName")
    public Role toRoleEntityFromRoleName(String roleName) {
        return roleRepository.findRoleByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    public abstract Role toEntity(UUID id, RoleCreateDto dto);
    public Role toEntity(RoleCreateDto dto) {
        return toEntity(UUID.randomUUID(), dto);
    }
    public abstract RoleCreateDto toCreateDto(Role entity);
    public abstract RoleResponseDto toResponseDto(Role entity);
    public abstract List<Role> toEntityList(List<RoleResponseDto> dtoList);
    public abstract List<RoleResponseDto> toDtoList(List<Role> entityList);

}
