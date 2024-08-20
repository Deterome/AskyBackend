package org.senla_project.application.dao;

import org.senla_project.application.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileDao extends Dao<Profile> {
    public ProfileDao() {
        super(Profile.class);
    }
}
