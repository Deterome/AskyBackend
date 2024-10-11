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
public abstract class ProfileMapper {
    @Mappings({
        @Mapping(source = "dto.birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "dto.username", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"}),
        @Mapping(source = "id", target = "profileId")
    })
    public abstract Profile toProfile(UUID id, ProfileCreateDto dto);
    public Profile toProfile(ProfileCreateDto dto) {
        return toProfile(null, dto);
    }
    @Mappings({
        @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "username", expression = "java(entity.getUser().getUsername())")
    })
    public abstract ProfileCreateDto toProfileCreateDto(Profile entity);
    @Mappings({
        @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "username", expression = "java(entity.getUser().getUsername())")
    })
    public abstract ProfileResponseDto toProfileResponseDto(Profile entity);
    public abstract List<Profile> toProfileList(List<ProfileResponseDto> dtoList);
    public abstract List<ProfileResponseDto> toProfileDtoList(List<Profile> entityList);

}
