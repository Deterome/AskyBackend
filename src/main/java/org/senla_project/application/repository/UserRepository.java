package org.senla_project.application.repository;

import org.senla_project.application.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface UserRepository extends DefaultDao<UUID, User> {
    Optional<User> findUserByUsername(String nickname);
}