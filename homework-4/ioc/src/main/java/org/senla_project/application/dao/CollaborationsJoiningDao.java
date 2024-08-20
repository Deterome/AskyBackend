package org.senla_project.application.dao;

import org.senla_project.application.entity.CollaborationsJoining;
import org.springframework.stereotype.Component;

@Component
public class CollaborationsJoiningDao extends Dao<CollaborationsJoining> {
    public CollaborationsJoiningDao() {
        super(CollaborationsJoining.class);
    }
}
