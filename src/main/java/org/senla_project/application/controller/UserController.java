package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.util.sort.SortOrder;
import org.senla_project.application.util.sort.UserSortType;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserController {

    Page<UserResponseDto> getAll(@Positive @Min(1) int pageNumber, @Positive @Min(1) int pageSize, UserSortType sortType, SortOrder sortOrder);

    UserResponseDto getById(@NonNull UUID id);

    UserResponseDto update(@NonNull UserUpdateDto userUpdateDto);

    void delete(@NonNull UserDeleteDto userDeleteDto);

    UserResponseDto getByUsername(@NonNull String username);

}