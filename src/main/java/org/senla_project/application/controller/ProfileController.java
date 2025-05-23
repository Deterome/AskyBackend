package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
import org.senla_project.application.util.sort.ProfileSortType;

import java.util.UUID;

public interface ProfileController extends CrudController<ProfileCreateDto, ProfileResponseDto, ProfileUpdateDto, ProfileDeleteDto, UUID, ProfileSortType> {

    ProfileResponseDto getByUsername(@NonNull String username);

}
