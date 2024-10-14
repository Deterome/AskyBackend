package org.senla_project.application.repository.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.User_;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends AbstractDao<UUID, User> implements UserRepository {
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public Optional<User> findUserByUsername(String nickname) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);

        Predicate equalsNickname = builder.equal(root.get(User_.username), nickname);

        query.select(root).where(equalsNickname);

        var results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
