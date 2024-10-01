package org.senla_project.application.repository;

import org.senla_project.application.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface RoleRepository extends DefaultDao<UUID, Role> {
    Optional<Role> findRoleByName(String roleName);
}
