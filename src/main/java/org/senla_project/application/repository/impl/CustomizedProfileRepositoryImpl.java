package org.senla_project.application.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.repository.CustomizedProfileRepository;

import java.util.Optional;

public class CustomizedProfileRepositoryImpl implements CustomizedProfileRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Profile> findByUsername(String username) {
        return em
                .createQuery("SELECT p FROM Profile AS p JOIN p.user AS u WHERE u.username = :username", Profile.class)
                .setParameter("username", username)
                .setHint("javax.persistence.fetchgraph", em.getEntityGraph("profile-entity-graph"))
                .getResultList()
                .stream()
                .findFirst();
    }
}
