package org.senla_project.application.service;

import org.senla_project.application.dto.admin.UserRoleCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;

public interface AdminService {

    UserResponseDto giveUserARole(UserRoleCreateDto userRoleCreateDto);

}
