package org.senla_project.application.service;

import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;

import java.util.UUID;

public interface ProfileService extends CrudService<ProfileCreateDto, ProfileResponseDto, ProfileUpdateDto, ProfileDeleteDto, UUID> {

    ProfileResponseDto getByUsername(String nickname);

}
