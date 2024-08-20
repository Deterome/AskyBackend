package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.entity.Profile;

import java.util.List;

@Mapper(componentModel = "spring", uses = ProfileMapper.class)
public interface ProfileListMapper {

    List<Profile> toEntityList(List<ProfileDto> dtoList);
    List<ProfileDto> toDtoList(List<Profile> entityList);

}


