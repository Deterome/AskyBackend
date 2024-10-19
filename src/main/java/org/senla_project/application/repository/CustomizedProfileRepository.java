package org.senla_project.application.repository;

import org.senla_project.application.entity.Profile;

import java.util.Optional;

public interface CustomizedProfileRepository {
    Optional<Profile> findByUsername(String username);

}
