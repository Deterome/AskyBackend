package org.senla_project.application.dao;

import org.senla_project.application.entity.Role;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RoleDao extends Dao<Role> {
    public RoleDao() {
        super(Role.class);
    }

    public Role findRoleByName(String roleName) {
        for (Role role: entities) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }

    public UUID findRoleId(String roleName) {
        Role role = findRoleByName(roleName);
        if (role == null) return null;
        return role.getId();
    }
}
