package org.senla_project.application.mapper;

import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.senla_project.application.dto.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.CollaborationsJoiningResponseDto;
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
    public abstract CollaborationsJoining toEntity(UUID id, CollaborationsJoiningCreateDto dto);
    public CollaborationsJoining toEntity(CollaborationsJoiningCreateDto dto) {
        return toEntity(null, dto);
    }
    @Mappings({
        @Mapping(source = "joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())"),
        @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    })
    public abstract CollaborationsJoiningCreateDto toCreateDto(CollaborationsJoining entity);
    @Mappings({
        @Mapping(source = "joinDate", target = "joinDate", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())"),
        @Mapping(target = "collabName", expression = "java(entity.getCollab().getCollabName())")
    })
    public abstract CollaborationsJoiningResponseDto toResponseDto(CollaborationsJoining entity);
    public abstract List<CollaborationsJoining> toEntityList(List<CollaborationsJoiningResponseDto> dtoList);
    public abstract List<CollaborationsJoiningResponseDto> toDtoList(List<CollaborationsJoining> entityList);
}
