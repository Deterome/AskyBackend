package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.admin.UserRoleCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;

public interface AdminController {

    UserResponseDto giveUserARole(@NonNull UserRoleCreateDto userRoleCreateDto);

}
