package org.senla_project.application.repository;

import org.senla_project.application.entity.Collaboration;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface CollaborationRepository extends DefaultDao<UUID, Collaboration> {
    Optional<Collaboration> findCollabByName(String collabName);
}
