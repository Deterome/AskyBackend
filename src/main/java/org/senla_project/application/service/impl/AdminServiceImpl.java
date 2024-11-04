package org.senla_project.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.admin.UserRoleCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.service.AdminService;
import org.senla_project.application.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserService userService;

    @Override
    @Transactional
    public UserResponseDto giveUserARole(UserRoleCreateDto userRoleCreateDto) {
        UserResponseDto userResponseDto = userService.getByUsername(userRoleCreateDto.getUsername());
        List<String> newRolesList = userResponseDto.getRoles();
        newRolesList.add(userRoleCreateDto.getRoleName());
        return userService.update(UserUpdateDto.builder()
                .userId(userResponseDto.getUserId())
                .roles(newRolesList)
                .build());
    }
}
