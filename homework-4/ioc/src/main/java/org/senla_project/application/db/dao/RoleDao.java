package org.senla_project.application.db.dao;

import org.senla_project.application.db.entities.Role;

public class RoleDao extends Dao<Role> {
    public RoleDao() {
        super(Role.class);
    }

    public Role findRoleByName(String roleName) {
        for (var role: entities) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }
}
