package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController implements CrudController<QuestionCreateDto, QuestionResponseDto, QuestionDeleteDto, UUID> {

    final private QuestionService questionService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuestionResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") @Positive @Min(1) int pageNumber, @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize) {
        return questionService.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return questionService.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponseDto create(@NonNull @RequestBody QuestionCreateDto element) {
        return questionService.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody QuestionCreateDto updatedElement) {
        return questionService.updateById(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody QuestionDeleteDto questionDeleteDto) {
        questionService.delete(questionDeleteDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto getByParams(@RequestParam(name = "header", required = false) String header,
                                           @RequestParam(name = "body", required = false) String body,
                                           @RequestParam(name = "author", required = false) String authorName) {
        return questionService.getByHeaderAndBodyAndAuthorName(header, body, authorName);
    }
}
