package org.senla_project.application.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.Question_;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.User_;
import org.senla_project.application.repository.CustomizedQuestionRepository;

import java.util.Optional;

public class CustomizedQuestionRepositoryImpl implements CustomizedQuestionRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Question> findByHeaderAndBodyAndAuthorName(String header, String body, String authorName) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);

        Root<Question> root = query.from(Question.class);
        Join<Question, User> userJoin = root.join(Question_.author, JoinType.LEFT);

        Predicate equalsAuthorName = builder.equal(userJoin.get(User_.username), authorName);
        Predicate equalsHeader = builder.equal(root.get(Question_.header), header);
        Predicate equalsBody = builder.equal(root.get(Question_.body), body);

        query.select(root).where(builder.and(equalsHeader, equalsBody, equalsAuthorName));

        var results = em.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
