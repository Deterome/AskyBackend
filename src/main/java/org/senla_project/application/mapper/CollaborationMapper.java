package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.dto.collaboration.CollabUpdateDto;
import org.senla_project.application.entity.Collaboration;

import java.util.List;
import java.util.UUID;

@Named("CollaborationMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class CollaborationMapper {

    public abstract Collaboration partialCollabToCollab(@MappingTarget Collaboration targetCollab, Collaboration collab);

    @Named("toCollabFromName")
    public Collaboration toCollabFromName(String collabName) {
        return Collaboration.builder()
                .collabName(collabName)
                .build();
    }

    @Mappings({
            @Mapping(source = "id", target = "collabId")
    })
    public abstract Collaboration toCollab(UUID id, CollabCreateDto dto);

    @Mappings({
            @Mapping(target = "collabId", ignore = true)
    })
    public abstract Collaboration toCollab(CollabCreateDto createDto);

    public abstract Collaboration toCollab(CollabUpdateDto updateDto);

    public abstract Collaboration toCollab(CollabResponseDto collabResponseDto);

    public abstract CollabCreateDto toCollabCreateDto(Collaboration entity);

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollabResponseDto toCollabResponseDto(Collaboration entity);

    public abstract List<Collaboration> toCollabList(List<CollabResponseDto> dtoList);

    public abstract List<CollabResponseDto> toCollabDtoList(List<Collaboration> entityList);

}
