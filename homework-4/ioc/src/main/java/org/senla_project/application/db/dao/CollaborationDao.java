package org.senla_project.application.db.dao;

import org.senla_project.application.db.entities.Collaboration;

public class CollaborationDao extends Dao<Collaboration> {
    public CollaborationDao() {
        super(Collaboration.class);
    }

    public Collaboration findCollabByName(String collabName) {
        for (var collab: entities) {
            if (collab.getCollabName().equals(collabName)) {
                return collab;
            }
        }
        return null;
    }
}
