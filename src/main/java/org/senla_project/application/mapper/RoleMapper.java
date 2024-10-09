package org.senla_project.application.mapper;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.entity.Role;

import java.util.*;
import java.util.stream.Collectors;

@Named("RoleMapper")
@Mapper(componentModel = "spring", uses = {UuidMapper.class})
@Slf4j
public abstract class RoleMapper {

    @Named("toRoleEntityFromName")
    public abstract Role toRoleEntityFromRoleName(String roleName);

    @Mapping(source = "id", target = "roleId")
    public abstract Role toRole(UUID id, RoleCreateDto dto);
    public Role toRole(RoleCreateDto dto) {
        return toRole(null, dto);
    }
    public abstract Role toRole(RoleResponseDto roleResponseDto);
    public abstract RoleCreateDto toRoleCreateDto(Role entity);
    public abstract RoleResponseDto toRoleResponseDto(Role entity);
    public abstract List<Role> toRoleList(List<RoleResponseDto> dtoList);
    public abstract List<RoleResponseDto> toRoleDtoList(List<Role> entityList);
    public List<Role> toRoleListFromStringList(List<String> rolesStringList) {
        return rolesStringList.stream().map(this::toRoleEntityFromRoleName).collect(Collectors.toList());
    }
    @Named("toRoleSetFromStringList")
    public Set<Role> toRoleSetFromStringList(List<String> rolesStringList) {
        return rolesStringList.stream().map(this::toRoleEntityFromRoleName).collect(Collectors.toSet());
    }
    @Named("toStringListFromRoleSet")
    public List<String> toStringListFromRoleSet(Set<Role> rolesSet) {
        return rolesSet.stream().map(Role::getRoleName).collect(Collectors.toList());
    }

}
