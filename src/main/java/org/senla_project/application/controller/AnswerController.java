package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.service.AnswerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController implements DefaultControllerInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    final private AnswerService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<AnswerResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") int pageNumber, @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return service.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerResponseDto create(@NonNull @RequestBody AnswerCreateDto element) {
        return service.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody AnswerCreateDto updatedElement) {
        return service.updateById(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable(name = "id") UUID id) {
        service.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto getByParams(@RequestParam(name = "author", required = false) String authorName,
                                         @RequestParam(name = "question_id", required = false) UUID questionId,
                                         @RequestParam(name = "body", required = false) String body) {
        return service.getByParams(authorName, questionId, body);
    }
}
