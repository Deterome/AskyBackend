package org.senla_project.application.dao;

import org.senla_project.application.entity.Entity;
import org.senla_project.application.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileDao extends Dao<Profile> {
    public ProfileDao() {
        super(Profile.class);
    }

    public Optional<UUID> findProfileId(String nickname) {
        return entities.stream()
                .filter(entity -> entity.getUser().getNickname().equals(nickname))
                .findFirst()
                .map(Entity::getId);
    }
}
