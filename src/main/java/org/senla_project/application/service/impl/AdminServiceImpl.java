package org.senla_project.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.admin.UserRoleCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.service.AdminService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    @Override
    public UserResponseDto giveUserARole(UserRoleCreateDto userRoleCreateDto) {
        return null;
    }
}
