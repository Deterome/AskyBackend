package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.entity.Collaboration;

import java.util.List;
import java.util.UUID;

@Named("CollaborationMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class})
public abstract class CollaborationMapper {

    @Named("toCollabFromName")
    public Collaboration toCollabFromName(String collabName) {
        return Collaboration.builder()
                .collabName(collabName)
                .build();
    }

    @Mappings({
            @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "id", target = "collabId")
    })
    public abstract Collaboration toCollab(UUID id, CollabCreateDto dto);

    @Mappings({
            @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "collabId", ignore = true)
    })
    public abstract Collaboration toCollab(CollabCreateDto dto);

    public abstract Collaboration toCollab(CollabResponseDto collabResponseDto);

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollabCreateDto toCollabCreateDto(Collaboration entity);

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollabResponseDto toCollabResponseDto(Collaboration entity);

    public abstract List<Collaboration> toCollabList(List<CollabResponseDto> dtoList);

    public abstract List<CollabResponseDto> toCollabDtoList(List<Collaboration> entityList);

}
