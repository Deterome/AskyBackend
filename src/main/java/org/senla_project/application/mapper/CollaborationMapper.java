package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.entity.Collaboration;

import java.util.List;
import java.util.UUID;

@Named("CollaborationMapper")
@Mapper(componentModel = "spring", uses = {UuidMapper.class})
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
    public abstract Collaboration toCollab(UUID id, CollaborationCreateDto dto);

    @Mappings({
            @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "collabId", ignore = true)
    })
    public abstract Collaboration toCollab(CollaborationCreateDto dto);

    public abstract Collaboration toCollab(CollaborationResponseDto collaborationResponseDto);

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollaborationCreateDto toCollabCreateDto(Collaboration entity);

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollaborationResponseDto toCollabResponseDto(Collaboration entity);

    public abstract List<Collaboration> toCollabList(List<CollaborationResponseDto> dtoList);

    public abstract List<CollaborationResponseDto> toCollabDtoList(List<Collaboration> entityList);

}
