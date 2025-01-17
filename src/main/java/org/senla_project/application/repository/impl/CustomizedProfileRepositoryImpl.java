package org.senla_project.application.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transaction;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.entity.Profile_;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.User_;
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

    @Override
    public void deleteByUsername(String username) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Profile> criteriaDelete = builder.createCriteriaDelete(Profile.class);

        Root<Profile> root = criteriaDelete.from(Profile.class);
        Join<Profile, User> userJoin = root.join(Profile_.user, JoinType.LEFT);

        Predicate equalsUsername = builder.equal(userJoin.get(User_.username), username);

        criteriaDelete.where(equalsUsername);

        em.createQuery(criteriaDelete).executeUpdate();
    }
}
