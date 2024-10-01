package org.senla_project.application.mapper;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
        @Mapping(source = "dto.userName", target = "user", qualifiedByName = {"UserMapper", "toUserFromName"}),
        @Mapping(source = "id", target = "profileId")
    })
    public abstract Profile toEntity(UUID id, ProfileCreateDto dto);
    public Profile toEntity(ProfileCreateDto dto) {
        return toEntity(null, dto);
    }
    @Mappings({
        @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())")
    })
    public abstract ProfileCreateDto toCreateDto(Profile entity);
    @Mappings({
        @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "userName", expression = "java(entity.getUser().getNickname())")
    })
    public abstract ProfileResponseDto toResponseDto(Profile entity);
    public abstract List<Profile> toEntityList(List<ProfileResponseDto> dtoList);
    public abstract List<ProfileResponseDto> toDtoList(List<Profile> entityList);

}
