package org.senla_project.application.repository;

import org.senla_project.application.entity.CollabRole;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface CollabRoleRepository extends PagingAndSortingRepository<CollabRole, UUID>, ListCrudRepository<CollabRole, UUID>, CustomizedCollabRoleRepository {
    Optional<CollabRole> findByCollabRoleName(String collabRoleName);

    boolean existsByCollabRoleName(String collabRoleName);

    void deleteByCollabRoleName(String collabRoleName);
}
