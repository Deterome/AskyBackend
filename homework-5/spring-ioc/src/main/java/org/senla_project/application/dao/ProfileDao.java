package org.senla_project.application.dao;

import org.senla_project.application.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ProfileDao extends DefaultDao<Profile> {
    Optional<Profile> findProfileByNickname(String nickname);
}
