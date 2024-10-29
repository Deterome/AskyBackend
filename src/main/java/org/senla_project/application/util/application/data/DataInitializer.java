package org.senla_project.application.util.application.data;

import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.service.CollabRoleService;
import org.senla_project.application.service.RoleService;
import org.senla_project.application.service.UserService;
import org.senla_project.application.util.enums.DefaultCollabRoles;
import org.senla_project.application.util.enums.DefaultRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {

    @Value("${application.firstAdmin.name}")
    private String firstAdminName;
    @Value("${application.firstAdmin.password}")
    private String firstAdminPassword;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CollabRoleService collabRoleService;

    public void initDefaultData() {
        addDefaultRolesToDatabase();
        addDefaultCollabRolesToDatabase();
        addPrimalAdminToDatabase();
    }

    private void addDefaultRolesToDatabase() {
        for (var defaultRoleEnum: DefaultRoles.values()) {
            if (!roleService.existByRoleName(defaultRoleEnum.toString())) {
                RoleCreateDto roleCreateDto = RoleCreateDto.builder()
                        .roleName(defaultRoleEnum.toString())
                        .build();
                roleService.create(roleCreateDto);
            }
        }
    }

    private void addDefaultCollabRolesToDatabase() {
        for (var defaultCollabRoleEnum: DefaultCollabRoles.values()) {
            if (!collabRoleService.existByCollabRoleName(defaultCollabRoleEnum.toString())) {
                CollabRoleCreateDto collabRoleCreateDto = CollabRoleCreateDto.builder()
                        .collabRoleName(defaultCollabRoleEnum.toString())
                        .build();
                collabRoleService.create(collabRoleCreateDto);
            }
        }
    }

    private void addPrimalAdminToDatabase() {
        if (!userService.existsByUsername(firstAdminName)) {
            UserCreateDto adminCreateDto = UserCreateDto.builder()
                    .username(firstAdminName)
                    .password(firstAdminPassword)
                    .roles(new ArrayList<>(List.of(DefaultRoles.USER.toString(), DefaultRoles.ADMIN.toString())))
                    .build();
            userService.create(adminCreateDto);
        }
    }

}
