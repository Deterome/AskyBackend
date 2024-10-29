package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.dto.answer.AnswerResponseDto;
import org.senla_project.application.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController implements CrudController<AnswerCreateDto, AnswerResponseDto, AnswerDeleteDto, UUID> {

    final private AnswerService answerService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<AnswerResponseDto> getAll(@RequestParam(name = "page", defaultValue = "1") @Positive @Min(1) int pageNumber, @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize) {
        return answerService.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return answerService.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerResponseDto create(@NonNull @RequestBody AnswerCreateDto element) {
        return answerService.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody AnswerCreateDto updatedElement) {
        return answerService.updateById(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody AnswerDeleteDto answerDeleteDto) {
        answerService.delete(answerDeleteDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto getByParams(@RequestParam(name = "author", required = false) String authorName, @RequestParam(name = "question_id", required = false) UUID questionId, @RequestParam(name = "body", required = false) String body) {
        return answerService.getByAuthorNameQuestionIdAndBody(authorName, questionId, body);
    }
}
