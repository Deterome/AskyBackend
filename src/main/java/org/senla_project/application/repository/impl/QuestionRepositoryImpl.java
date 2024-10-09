package org.senla_project.application.repository.impl;

import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.QuestionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class QuestionRepositoryImpl extends AbstractDao<UUID, Question> implements QuestionRepository {
    @Override
    protected Class<Question> getEntityClass() {
        return Question.class;
    }

    @Override
    public Optional<Question> findQuestion(String header, String body, String authorName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);

        Root<Question> root = query.from(Question.class);
        Join<Question, User> userJoin = root.join(Question_.author, JoinType.LEFT);

        Predicate equalsAuthorName = builder.equal(userJoin.get(User_.username), authorName);
        Predicate equalsHeader = builder.equal(root.get(Question_.header), header);
        Predicate equalsBody = builder.equal(root.get(Question_.body), body);

        query.select(root).where(builder.and(equalsHeader, equalsBody, equalsAuthorName));

        var results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
