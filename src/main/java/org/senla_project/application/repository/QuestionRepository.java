package org.senla_project.application.repository;

import org.senla_project.application.entity.Question;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public interface QuestionRepository extends DefaultDao<UUID, Question> {
    Optional<Question> findQuestion(String header, String body, String authorName);
}
