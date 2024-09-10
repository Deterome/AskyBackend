package org.senla_project.application.dao;

import org.senla_project.application.entity.Question;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface QuestionDao extends DefaultDao<Question> {
    Optional<Question> findQuestion(String header, String body, String authorName);
}
