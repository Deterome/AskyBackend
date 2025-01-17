package org.senla_project.application.repository;

import org.senla_project.application.entity.Question;

import java.util.Optional;

public interface CustomizedQuestionRepository {
    Optional<Question> findByHeaderAndBodyAndAuthorName(String header, String body, String authorName);
}
