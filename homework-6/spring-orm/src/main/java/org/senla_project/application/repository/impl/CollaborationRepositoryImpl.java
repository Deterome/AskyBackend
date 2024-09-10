package org.senla_project.application.repository.impl;

import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.CollaborationRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationRepositoryImpl extends AbstractDao<UUID, Collaboration> implements CollaborationRepository {
    @Override
    protected Class<Collaboration> getEntityClass() {
        return Collaboration.class;
    }

    @Override
    public Optional<Collaboration> findCollabByName(String collabName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Collaboration> query = builder.createQuery(Collaboration.class);

        Root<Collaboration> root = query.from(Collaboration.class);

        Predicate equalsCollabName = builder.equal(root.get(Collaboration_.collabName), collabName);

        query.select(root).where(equalsCollabName);

        var results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
