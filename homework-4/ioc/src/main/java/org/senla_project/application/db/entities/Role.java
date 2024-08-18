package org.senla_project.application.db.entities;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Role extends Entity {

    private String roleName;

    @Override
    public Role clone() {
        Role clone = (Role) super.clone();
        clone.roleName = roleName;
        return clone;
    }

}
