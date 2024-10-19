package org.senla_project.application.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.CustomizedCollaborationsJoiningRepository;

import java.util.Optional;

public class CustomizedCollaborationsJoiningRepositoryImpl implements CustomizedCollaborationsJoiningRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<CollaborationsJoining> findByUsernameAndCollabName(String username, String collabName) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<CollaborationsJoining> query = builder.createQuery(CollaborationsJoining.class);

        Root<CollaborationsJoining> root = query.from(CollaborationsJoining.class);
        Join<CollaborationsJoining, User> userJoin = root.join(CollaborationsJoining_.user, JoinType.LEFT);
        Join<CollaborationsJoining, Collaboration> collabJoin = root.join(CollaborationsJoining_.collab, JoinType.LEFT);

        Predicate equalsUsername = builder.equal(userJoin.get(User_.username), username);
        Predicate equalsCollabName = builder.equal(collabJoin.get(Collaboration_.collabName), collabName);

        query.select(root).where(builder.and(equalsUsername, equalsCollabName));

        var results = em.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
