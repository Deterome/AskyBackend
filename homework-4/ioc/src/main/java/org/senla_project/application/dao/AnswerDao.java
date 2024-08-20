package org.senla_project.application.dao;

import org.senla_project.application.entity.Answer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AnswerDao extends Dao<Answer> {
    public AnswerDao() {
        super(Answer.class);
    }

    public UUID findAnswerId(String authorName, UUID questionId, String body) {
        for (Answer entity: entities) {
            if (entity.getAuthor().getNickname().equals(authorName)
                    && entity.getQuestion().getId().equals(questionId)
                    && entity.getBody().equals(body))
                return entity.getId();
        }
        return null;
    }
}