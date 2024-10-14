package org.senla_project.application.repository.impl;

import jakarta.persistence.criteria.*;
import org.senla_project.application.entity.*;
import org.senla_project.application.repository.AbstractDao;
import org.senla_project.application.repository.AnswerRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class AnswerRepositoryImpl extends AbstractDao<UUID, Answer> implements AnswerRepository {
    @Override
    protected Class<Answer> getEntityClass() {
        return Answer.class;
    }

    @Override
    public Optional<Answer> findAnswer(String authorName, UUID questionId, String body) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Answer> query = builder.createQuery(Answer.class);

        Root<Answer> root = query.from(Answer.class);
        Join<Answer, User> userJoin = root.join(Answer_.author, JoinType.LEFT);
        Join<Answer, Question> questionJoin = root.join(Answer_.question, JoinType.LEFT);

        Predicate equalsAuthorName = builder.equal(userJoin.get(User_.username), authorName);
        Predicate equalsQuestionId = builder.equal(questionJoin.get(Question_.questionId), questionId);
        Predicate equalsBody = builder.equal(root.get(Answer_.body), body);

        query.select(root).where(builder.and(equalsBody, equalsQuestionId, equalsAuthorName));

        var results = entityManager.createQuery(query).getResultList();
        if (results.isEmpty()) return Optional.empty();
        return Optional.of(results.getFirst());
    }
}
