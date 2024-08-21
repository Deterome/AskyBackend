package org.senla_project.application.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.entity.Profile;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface ProfileMapper {

    @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "userName", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"})
    Profile toEntity(ProfileDto dto);
    @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())")
    ProfileDto toDto(Profile entity);
    List<Profile> toEntityList(List<ProfileDto> dtoList);
    List<ProfileDto> toDtoList(List<Profile> entityList);

}
