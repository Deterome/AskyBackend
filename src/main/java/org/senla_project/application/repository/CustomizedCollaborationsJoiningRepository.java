package org.senla_project.application.repository;

import org.senla_project.application.entity.CollaborationsJoining;

import java.util.Optional;

public interface CustomizedCollaborationsJoiningRepository {
    Optional<CollaborationsJoining> findByUsernameAndCollabName(String username, String collabName);
}
