package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.collabRole.CollabRoleResponseDto;
import org.senla_project.application.dto.collabRole.CollabRoleUpdateDto;
import org.senla_project.application.entity.CollabRole;

import java.util.List;
import java.util.UUID;

@Named("CollabRoleMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UuidMapper.class})
public abstract class CollabRoleMapper {

    @Named("toCollabRoleFromName")
    public CollabRole toCollabRoleFromName(String collabRoleName) {
        return CollabRole.builder()
                .collabRoleName(collabRoleName)
                .build();
    }

    @Mappings({
            @Mapping(source = "id", target = "collabRoleId")
    })
    public abstract CollabRole toCollabRole(UUID id, CollabRoleCreateDto collabRoleCreateDto);

    @Mappings({
            @Mapping(target = "collabRoleId", ignore = true)
    })
    public abstract CollabRole toCollabRole(CollabRoleCreateDto collabRoleCreateDto);

    public abstract CollabRole toCollabRole(CollabRoleUpdateDto updateDto);

    public abstract CollabRole toCollabRole(CollabRoleResponseDto collabRoleResponseDto);

    public abstract CollabRoleCreateDto toCollabRoleCreateDto(CollabRole collabRole);

    public abstract CollabRoleResponseDto toCollabRoleResponseDto(CollabRole collabRole);

    public abstract List<CollabRole> toCollabRoleList(List<CollabRoleResponseDto> collabRoleResponseDtoList);

    public abstract List<CollabRoleResponseDto> toCollabRoleResponseDtoList(List<CollabRole> collabRoleList);

}
