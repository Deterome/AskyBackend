package org.senla_project.application.dao;

import org.senla_project.application.entity.CollaborationsJoining;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CollaborationsJoiningDao extends Dao<CollaborationsJoining> {
    public CollaborationsJoiningDao() {
        super(CollaborationsJoining.class);
    }

    public UUID findCollaborationJoinId(String username, String collaboration) {
        for (CollaborationsJoining entity: entities) {
            if (entity.getUser().getNickname().equals(username)
                    && entity.getCollab().getCollabName().equals(collaboration))
                return entity.getId();
        }
        return null;
    }
}
