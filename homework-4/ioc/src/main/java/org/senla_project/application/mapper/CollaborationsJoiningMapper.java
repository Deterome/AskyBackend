package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.CollaborationsJoining;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, CollaborationMapper.class})
public interface CollaborationsJoiningMapper {

    @Mapping(source = "joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "userName", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"})
    @Mapping(source = "collabName", target = "collab", qualifiedByName = {"CollaborationMapper", "toCollabFromName"})
    CollaborationsJoining toEntity(CollaborationsJoiningDto dto);
    @Mapping(source = "joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())")
    @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    CollaborationsJoiningDto toDto(CollaborationsJoining entity);
    List<CollaborationsJoining> toEntityList(List<CollaborationsJoiningDto> dtoList);
    List<CollaborationsJoiningDto> toDtoList(List<CollaborationsJoining> entityList);
}
