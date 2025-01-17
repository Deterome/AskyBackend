package org.senla_project.application.util.data;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.CollabRoleRepository;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    @Value("${application.firstAdmin.name}")
    private String firstAdminName;
    @Value("${application.firstAdmin.password}")
    private String firstAdminPassword;

    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private RoleRepository roleRepository;
    final private CollabRoleRepository collabRoleRepository;

    public void initDefaultData() {
        addDefaultRolesToDatabase();
        addDefaultCollabRolesToDatabase();
        addPrimalAdminToDatabase();
    }

    private void addDefaultRolesToDatabase() {
        for (var defaultRoleEnum: DefaultRole.values()) {
            if (!roleRepository.existsByRoleName(defaultRoleEnum.toString())) {
                Role role = Role.builder()
                        .roleName(defaultRoleEnum.toString())
                        .build();
                roleRepository.save(role);
            }
        }
    }

    private void addDefaultCollabRolesToDatabase() {
        for (var defaultCollabRoleEnum: DefaultCollabRole.values()) {
            if (!collabRoleRepository.existsByCollabRoleName(defaultCollabRoleEnum.toString())) {
                CollabRole collabRoleCreateDto = CollabRole.builder()
                        .collabRoleName(defaultCollabRoleEnum.toString())
                        .build();
                collabRoleRepository.save(collabRoleCreateDto);
            }
        }
    }

    private void addPrimalAdminToDatabase() {
        if (!userRepository.existsByUsername(firstAdminName)) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByRoleName(DefaultRole.USER.toString()).get());
            adminRoles.add(roleRepository.findByRoleName(DefaultRole.ADMIN.toString()).get());
            User admin = User.builder()
                    .username(firstAdminName)
                    .password(passwordEncoder.encode(firstAdminPassword))
                    .roles(adminRoles)
                    .build();
            userRepository.save(admin);
        }
    }

}
