package org.senla_project.application.dao;

import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.Entity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerDao extends Dao<Answer> {
    public AnswerDao() {
        super(Answer.class);
    }

    public Optional<UUID> findAnswerId(String authorName, UUID questionId, String body) {
        return entities.stream()
                .filter(entity -> entity.getAuthor().getNickname().equals(authorName)
                    && entity.getQuestion().getId().equals(questionId)
                    && entity.getBody().equals(body))
                .findFirst()
                .map(Entity::getId);
    }
}