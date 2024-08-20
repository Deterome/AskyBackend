package org.senla_project.application.dao;

import org.senla_project.application.entity.Collaboration;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CollaborationDao extends Dao<Collaboration> {
    public CollaborationDao() {
        super(Collaboration.class);
    }

    public Collaboration findCollabByName(String collabName) {
        for (Collaboration collab: entities) {
            if (collab.getCollabName().equals(collabName)) {
                return collab;
            }
        }
        return null;
    }

    public UUID findCollabId(String collabName) {
        Collaboration collab = findCollabByName(collabName);
        if (collab == null) return null;
        return collab.getId();
    }
}
