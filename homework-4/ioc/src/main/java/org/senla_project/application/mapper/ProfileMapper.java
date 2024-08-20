package org.senla_project.application.mapper;


import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.entity.Profile;

public interface ProfileMapper {

    Profile toEntity(ProfileDto dto);
    ProfileDto toDto(Profile entity);

}
