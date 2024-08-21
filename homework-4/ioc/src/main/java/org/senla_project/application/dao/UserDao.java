package org.senla_project.application.dao;

import org.senla_project.application.entity.Entity;
import org.senla_project.application.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserDao extends Dao<User> {
    public UserDao() {
        super(User.class);
    }

    public Optional<User> findUserByNickname(String nickname) {
        return entities.stream()
                .filter(entity -> entity.getNickname().equals(nickname))
                .findFirst();
    }

    public Optional<UUID> findUserId(String nickname) {
        return findUserByNickname(nickname).map(Entity::getId);
    }
}