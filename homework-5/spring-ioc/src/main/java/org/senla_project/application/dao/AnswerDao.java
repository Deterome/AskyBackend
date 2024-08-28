package org.senla_project.application.dao;

import org.senla_project.application.entity.Answer;

import java.util.Optional;
import java.util.UUID;

public interface AnswerDao extends DefaultDao<Answer> {
    Optional<Answer> findAnswer(String authorName, UUID questionId, String body);
}
