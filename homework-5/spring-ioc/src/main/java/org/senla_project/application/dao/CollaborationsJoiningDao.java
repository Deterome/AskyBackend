package org.senla_project.application.dao;

import org.senla_project.application.entity.CollaborationsJoining;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CollaborationsJoiningDao extends DefaultDao<CollaborationsJoining> {
    Optional<CollaborationsJoining> findCollaborationJoin(String username, String collaboration);
}
