package org.senla_project.application.entity;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data @Builder
public class Role extends Entity {

    private String roleName;

    @Override
    public Role clone() {
        Role clone = (Role) super.clone();
        clone.roleName = roleName;
        return clone;
    }

}
