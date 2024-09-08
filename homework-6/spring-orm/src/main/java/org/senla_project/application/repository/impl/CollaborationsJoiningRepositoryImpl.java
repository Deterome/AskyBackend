package org.senla_project.application.repository.impl;

import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationsJoiningRepositoryImpl extends AbstractDao<UUID, CollaborationsJoining> implements CollaborationsJoiningRepository {
    @Override
    protected Class<CollaborationsJoining> getEntityClass() {
        return CollaborationsJoining.class;
    }

    @Override
    public Optional<CollaborationsJoining> findCollaborationJoin(String username, String collabName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CollaborationsJoining> query = builder.createQuery(CollaborationsJoining.class);

        Root<CollaborationsJoining> root = query.from(CollaborationsJoining.class);
        Join<CollaborationsJoining, User> userJoin = root.join(CollaborationsJoining_.user, JoinType.LEFT);
        Join<CollaborationsJoining, Collaboration> collabJoin = root.join(CollaborationsJoining_.collab, JoinType.LEFT);

        Predicate equalsUsername = builder.equal(userJoin.get(User_.nickname), username);
        Predicate equalsCollabName = builder.equal(collabJoin.get(Collaboration_.collabName), collabName);

        query.select(root).where(builder.and(equalsUsername, equalsCollabName));

        var results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
