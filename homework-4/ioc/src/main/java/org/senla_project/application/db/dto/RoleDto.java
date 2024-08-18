package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.Role;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class RoleDto {

    private UUID roleId;
    private String roleName;

    public RoleDto(@NonNull Role role) {
        this.roleId = role.getId();
        this.roleName = role.getRoleName();
    }

}
