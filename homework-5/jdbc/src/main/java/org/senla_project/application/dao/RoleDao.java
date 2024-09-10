package org.senla_project.application.dao;

import org.senla_project.application.entity.Role;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RoleDao extends DefaultDao<Role> {
    Optional<Role> findRoleByName(String roleName);
}
