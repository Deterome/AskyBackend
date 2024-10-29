package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleCreateDto;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleResponseDto;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.UserCollaborationCollabRole;
import org.senla_project.application.entity.identifiers.UserCollaborationCollabRoleId;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CollaborationMapper.class, CollabRoleMapper.class, UuidMapper.class})
public abstract class UserCollaborationCollabRoleMapper {


    public UserCollaborationCollabRole toUserCollaborationCollabRole(UserCollaborationCollabRoleId id, UserCollaborationCollabRoleCreateDto dto) {
        return UserCollaborationCollabRole.builder()
                .user(User.builder().username(dto.getUsername()).userId(id.getUser()).build())
                .collab(Collaboration.builder().collabName(dto.getCollabName()).collabId(id.getCollab()).build())
                .collabRole(CollabRole.builder().collabRoleName(dto.getCollabRoleName()).collabRoleId(id.getCollabRole()).build())
                .build();
    }

    @Mappings({
            @Mapping(source = "dto.collabRoleName", target = "collabRole", qualifiedByName = {"CollabRoleMapper", "toCollabRoleFromName"}),
            @Mapping(source = "dto.username", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"}),
            @Mapping(source = "dto.collabName", target = "collab", qualifiedByName = {"CollaborationMapper", "toCollabFromName"}),
    })
    public abstract UserCollaborationCollabRole toUserCollaborationCollabRole(UserCollaborationCollabRoleCreateDto dto);

    @Mappings({
            @Mapping(target = "collabRoleName", expression = "java(entity.getCollabRole().getCollabRoleName())"),
            @Mapping(target = "username", expression = "java(entity.getUser().getUsername())"),
            @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    })
    public abstract UserCollaborationCollabRoleCreateDto toUserCollaborationCollabRoleCreateDto(UserCollaborationCollabRole entity);

    @Mappings({
            @Mapping(target = "collabRoleName", expression = "java(entity.getCollabRole().getCollabRoleName())"),
            @Mapping(target = "username", expression = "java(entity.getUser().getUsername())"),
            @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    })
    public abstract UserCollaborationCollabRoleResponseDto toUserCollaborationCollabRoleResponseDto(UserCollaborationCollabRole entity);

    public abstract List<UserCollaborationCollabRole> toUserCollaborationCollabRoleList(List<UserCollaborationCollabRoleResponseDto> dtoList);

    public abstract List<UserCollaborationCollabRoleResponseDto> toUserCollaborationCollabRoleResponseDtoList(List<UserCollaborationCollabRole> entityList);
}
