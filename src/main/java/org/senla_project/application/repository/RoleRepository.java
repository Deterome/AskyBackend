package org.senla_project.application.repository;

import org.senla_project.application.entity.Role;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, UUID>, ListCrudRepository<Role, UUID> {
    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);

    void deleteByRoleName(String roleName);
}
