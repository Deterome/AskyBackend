package org.senla_project.application.db.entities;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Role extends Entity {

    private UUID roleId;
    private String roleName;

}
