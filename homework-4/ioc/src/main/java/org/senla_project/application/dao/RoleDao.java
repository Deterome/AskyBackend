package org.senla_project.application.dao;

import org.senla_project.application.entity.Entity;
import org.senla_project.application.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RoleDao extends Dao<Role> {
    public RoleDao() {
        super(Role.class);
    }

    public Optional<Role> findRoleByName(String roleName) {
        return entities.stream()
                .filter(entity -> entity.getRoleName().equals(roleName))
                .findFirst();
    }

    public Optional<UUID> findRoleId(String roleName) {
        return findRoleByName(roleName).map(Entity::getId);
    }
}
