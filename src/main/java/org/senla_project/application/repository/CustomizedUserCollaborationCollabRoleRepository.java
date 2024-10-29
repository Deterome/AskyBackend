package org.senla_project.application.repository;

public interface CustomizedUserCollaborationCollabRoleRepository {
    void deleteByUsernameAndCollabNameAndCollabRoleName(String username, String collabName, String collabRoleName);
}
