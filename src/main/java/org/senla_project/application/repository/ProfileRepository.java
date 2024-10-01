package org.senla_project.application.repository;

import org.senla_project.application.entity.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface ProfileRepository extends DefaultDao<UUID, Profile> {
    Optional<Profile> findProfileByNickname(String nickname);
}
