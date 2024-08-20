package org.senla_project.application.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor @Data @Builder
public class RoleDto {

    private UUID roleId;
    private String roleName;

}
