package org.senla_project.application.dao;

import org.senla_project.application.entity.Entity;
import org.senla_project.application.entity.Question;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionDao extends Dao<Question> {
    public QuestionDao() {
        super(Question.class);
    }

    public Optional<UUID> findQuestionId(String header, String body, String authorName) {
        return entities.stream()
                .filter(entity -> entity.getHeader().equals(header)
                    && entity.getBody().equals(body)
                    && entity.getAuthor().getNickname().equals(authorName))
                .findFirst()
                .map(Entity::getId);
    }
}
