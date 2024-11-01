package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.dto.question.QuestionUpdateDto;

import java.util.UUID;

public interface QuestionController extends CrudController<QuestionCreateDto, QuestionResponseDto, QuestionUpdateDto, QuestionDeleteDto, UUID> {

    QuestionResponseDto getByHeaderAndBodyAndAuthorName(@NonNull String header, @NonNull String body, @NonNull String authorName);

}
