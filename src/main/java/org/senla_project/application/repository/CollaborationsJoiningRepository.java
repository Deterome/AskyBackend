package org.senla_project.application.repository;

import org.senla_project.application.entity.CollaborationsJoining;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface CollaborationsJoiningRepository extends DefaultDao<UUID, CollaborationsJoining> {
    Optional<CollaborationsJoining> findCollabJoin(String username, String collaboration);
}
