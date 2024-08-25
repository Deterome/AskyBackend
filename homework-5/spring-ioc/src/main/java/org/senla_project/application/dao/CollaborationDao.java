package org.senla_project.application.dao;

import org.senla_project.application.entity.Collaboration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CollaborationDao extends DefaultDao<Collaboration> {
    Optional<Collaboration> findCollabByName(String collabName);
}
