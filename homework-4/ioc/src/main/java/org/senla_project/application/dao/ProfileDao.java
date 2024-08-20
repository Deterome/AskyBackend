package org.senla_project.application.dao;

import org.senla_project.application.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProfileDao extends Dao<Profile> {
    public ProfileDao() {
        super(Profile.class);
    }

    public UUID findProfileId(String nickname) {
        for (Profile entity: entities) {
            if (entity.getUser().getNickname().equals(nickname))
                return entity.getId();
        }
        return null;
    }
}
