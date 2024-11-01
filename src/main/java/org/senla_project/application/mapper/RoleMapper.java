package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleResponseDto;
import org.senla_project.application.dto.role.RoleUpdateDto;
import org.senla_project.application.entity.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Named("RoleMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class, UserMapper.class})
public abstract class RoleMapper {

    @Named("toRoleEntityFromName")
    public Role toRoleEntityFromRoleName(String roleName) {
        return Role.builder()
                .roleName(roleName)
                .build();
    }

    @Mapping(source = "id", target = "roleId")
    public abstract Role toRole(UUID id, RoleCreateDto dto);

    @Mapping(target = "roleId", ignore = true)
    public abstract Role toRole(RoleCreateDto dto);

    public abstract Role toRole(RoleUpdateDto updateDto);

    @Mapping(source = "users", target = "users", qualifiedByName = {"UserMapper", "toUserSetFromStringList"})
    public abstract Role toRole(RoleResponseDto roleResponseDto);

    public abstract RoleCreateDto toRoleCreateDto(Role entity);

    @Mapping(source = "users", target = "users", qualifiedByName = {"UserMapper", "toStringListFromUserSet"})
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
