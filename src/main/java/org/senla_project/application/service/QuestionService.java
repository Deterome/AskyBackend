package org.senla_project.application.service;

import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;

import java.util.UUID;

public interface QuestionService extends CrudService<QuestionCreateDto, QuestionResponseDto, QuestionDeleteDto, UUID> {
    QuestionResponseDto getByHeaderAndBodyAndAuthorName(String header, String body, String authorName);
}
