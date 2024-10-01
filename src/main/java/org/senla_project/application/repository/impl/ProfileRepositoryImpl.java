package org.senla_project.application.repository.impl;

import org.senla_project.application.entity.*;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.ProfileRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class ProfileRepositoryImpl extends AbstractDao<UUID, Profile> implements ProfileRepository {
    @Override
    protected Class<Profile> getEntityClass() {
        return Profile.class;
    }

    @Override
    public Optional<Profile> findProfileByNickname(String nickname) {
        return entityManager
                .createQuery("SELECT p FROM Profile AS p JOIN p.user AS u WHERE u.nickname = :nickname", Profile.class)
                .setParameter("nickname", nickname)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("profile-entity-graph"))
                .getResultList()
                .stream()
                .findFirst();
    }
}
