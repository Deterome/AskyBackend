package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.admin.UserRoleCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/users/give_role")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto giveUserARole(@NonNull @RequestBody UserRoleCreateDto userRoleCreateDto) {
        return adminService.giveUserARole(userRoleCreateDto);
    }

}
