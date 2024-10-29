package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningResponseDto;
import org.senla_project.application.entity.CollaborationsJoining;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CollaborationMapper.class, UuidMapper.class})
public abstract class CollaborationsJoiningMapper {

    @Mappings({
            @Mapping(source = "dto.joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "dto.userName", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"}),
            @Mapping(source = "dto.collabName", target = "collab", qualifiedByName = {"CollaborationMapper", "toCollabFromName"}),
            @Mapping(source = "id", target = "joinId")
    })
    public abstract CollaborationsJoining toCollabJoin(UUID id, CollaborationsJoiningCreateDto dto);

    @Mappings({
            @Mapping(source = "dto.joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "dto.userName", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"}),
            @Mapping(source = "dto.collabName", target = "collab", qualifiedByName = {"CollaborationMapper", "toCollabFromName"}),
            @Mapping(target = "joinId", ignore = true)
    })
    public abstract CollaborationsJoining toCollabJoin(CollaborationsJoiningCreateDto dto);

    @Mappings({
            @Mapping(source = "joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "userName", expression = "java(entity.getUser().getUsername())"),
            @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    })
    public abstract CollaborationsJoiningCreateDto toCollabJoinCreateDto(CollaborationsJoining entity);

    @Mappings({
            @Mapping(source = "joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "userName", expression = "java(entity.getUser().getUsername())"),
            @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    })
    public abstract CollaborationsJoiningResponseDto toCollabJoinResponseDto(CollaborationsJoining entity);

    public abstract List<CollaborationsJoining> toCollabJoinList(List<CollaborationsJoiningResponseDto> dtoList);

    public abstract List<CollaborationsJoiningResponseDto> toCollabJoinDtoList(List<CollaborationsJoining> entityList);
}
