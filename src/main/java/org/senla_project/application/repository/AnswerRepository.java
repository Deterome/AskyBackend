package org.senla_project.application.repository;

import org.senla_project.application.entity.Answer;

import java.util.Optional;
import java.util.UUID;

public interface AnswerRepository extends DefaultDao<UUID, Answer> {
    Optional<Answer> findAnswer(String authorName, UUID questionId, String body);
}
