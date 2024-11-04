package org.senla_project.application.controller.impl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.controller.QuestionController;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.dto.question.QuestionUpdateDto;
import org.senla_project.application.service.QuestionService;
import org.senla_project.application.util.sort.QuestionSortType;
import org.senla_project.application.util.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionControllerImpl implements QuestionController {

    final private QuestionService questionService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuestionResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") @Positive @Min(1) int pageNumber,
                                            @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize,
                                            @RequestParam(name = "sort", defaultValue = "CreateTime") QuestionSortType sortType,
                                            @RequestParam(name = "order", defaultValue = "Ascending") SortOrder sortOrder) {
        return questionService.getAll(PageRequest.of(
                pageNumber - 1,
                pageSize,
                sortOrder.equals(SortOrder.ASCENDING) ?
                        Sort.by(sortType.getSortingFieldName()).ascending() :
                        Sort.by(sortType.getSortingFieldName()).descending()
        ));
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
    public QuestionResponseDto create(@NonNull @RequestBody QuestionCreateDto questionCreateDto) {
        return questionService.create(questionCreateDto);
    }

    @Override
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto update(@NonNull @RequestBody QuestionUpdateDto questionUpdateDto) {
        return questionService.update(questionUpdateDto);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody QuestionDeleteDto questionDeleteDto) {
        questionService.delete(questionDeleteDto);
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto getByHeaderAndBodyAndAuthorName(@RequestParam(name = "header", required = false) @NonNull String header,
                                                               @RequestParam(name = "body", required = false) @NonNull String body,
                                                               @RequestParam(name = "author", required = false) @NonNull String authorName) {
        return questionService.getByHeaderAndBodyAndAuthorName(header, body, authorName);
    }

}
