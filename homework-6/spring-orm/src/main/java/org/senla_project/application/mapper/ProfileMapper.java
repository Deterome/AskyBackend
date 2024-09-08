package org.senla_project.application.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.entity.Profile;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, UuidMapper.class})
public interface ProfileMapper {
    @Mappings({
        @Mapping(source = "dto.birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "dto.userName", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"})
    })
    Profile toEntity(UUID id, ProfileCreateDto dto);
    default Profile toEntity(ProfileCreateDto dto) {
        return toEntity(UUID.randomUUID(), dto);
    }
    @Mappings({
        @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())")
    })
    ProfileCreateDto toCreateDto(Profile entity);
    @Mappings({
        @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())")
    })
    ProfileResponseDto toResponseDto(Profile entity);
    List<Profile> toEntityList(List<ProfileResponseDto> dtoList);
    List<ProfileResponseDto> toDtoList(List<Profile> entityList);

}
