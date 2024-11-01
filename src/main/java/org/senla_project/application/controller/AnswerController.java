package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.dto.answer.AnswerResponseDto;
import org.senla_project.application.dto.answer.AnswerUpdateDto;

import java.util.UUID;

public interface AnswerController extends CrudController<AnswerCreateDto, AnswerResponseDto, AnswerUpdateDto, AnswerDeleteDto, UUID> {

    AnswerResponseDto getByAuthorNameQuestionIdAndBody(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body);

}
