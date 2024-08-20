package org.senla_project.application.dao;

import lombok.NonNull;
import org.senla_project.application.entity.Question;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class QuestionDao extends Dao<Question> {
    public QuestionDao() {
        super(Question.class);
    }

    public UUID findQuestionId(String header, String body, String authorName) {
        for (Question entity: entities) {
            if (entity.getHeader().equals(header)
                    && entity.getBody().equals(body)
                    && entity.getAuthor().getNickname().equals(authorName))
                return entity.getId();
        }
        return null;
    }
}
