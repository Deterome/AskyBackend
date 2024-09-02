package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Named("CollaborationMapper")
@Mapper(componentModel = "spring")
public abstract class CollaborationMapper {

    @Autowired
    private CollaborationRepository collabDao;

    @Named("toCollabFromName")
    public Collaboration toCollabFromName(String collabName) {
        return collabDao.findCollabByName(collabName).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract Collaboration toEntity(CollaborationDto dto);
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    public abstract CollaborationDto toDto(Collaboration entity);
    public abstract List<Collaboration> toEntityList(List<CollaborationDto> dtoList);
    public abstract List<CollaborationDto> toDtoList(List<Collaboration> entityList);

}
