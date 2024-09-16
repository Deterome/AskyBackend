package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Named("CollaborationMapper")
@Mapper(componentModel = "spring", uses = {UuidMapper.class})
public abstract class CollaborationMapper {

    @Autowired
    private CollaborationRepository collabDao;

    @Named("toCollabFromName")
    public Collaboration toCollabFromName(String collabName) {
        return collabDao.findCollabByName(collabName).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract Collaboration toEntity(UUID id, CollaborationCreateDto dto);
    public Collaboration toEntity(CollaborationCreateDto dto) {
        return toEntity(UUID.randomUUID(), dto);
    }
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollaborationCreateDto toCreateDto(Collaboration entity);
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollaborationResponseDto toResponseDto(Collaboration entity);
    public abstract List<Collaboration> toEntityList(List<CollaborationResponseDto> dtoList);
    public abstract List<CollaborationResponseDto> toDtoList(List<Collaboration> entityList);

}
