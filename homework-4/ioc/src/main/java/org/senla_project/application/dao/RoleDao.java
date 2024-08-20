package org.senla_project.application.dao;

import org.senla_project.application.entity.Role;
import org.springframework.stereotype.Component;

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
}
