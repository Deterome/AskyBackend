package org.senla_project.application.dao;

import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.entity.Entity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationsJoiningDao extends Dao<CollaborationsJoining> {
    public CollaborationsJoiningDao() {
        super(CollaborationsJoining.class);
    }

    public Optional<UUID> findCollaborationJoinId(String username, String collaboration) {
        return entities.stream()
                .filter(entity -> entity.getUser().getNickname().equals(username)
                    && entity.getCollab().getCollabName().equals(collaboration))
                .findFirst()
                .map(Entity::getId);
    }
}
