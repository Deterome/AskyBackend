package org.senla_project.application.dao;

import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.Entity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationDao extends Dao<Collaboration> {
    public CollaborationDao() {
        super(Collaboration.class);
    }

    public Optional<Collaboration> findCollabByName(String collabName) {
        return entities.stream()
                .filter(entity -> entity.getCollabName().equals(collabName))
                .findFirst();
    }

    public Optional<UUID> findCollabId(String collabName) {
        return findCollabByName(collabName).map(Entity::getId);
    }
}
