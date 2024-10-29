package org.senla_project.application.repository;

import org.senla_project.application.entity.CollabRole;

import java.util.List;

public interface CustomizedCollabRoleRepository {

    List<CollabRole> findByUsernameAndCollabName(String username, String collabName);
    List<CollabRole> findByCollabName(String collabName);

}
