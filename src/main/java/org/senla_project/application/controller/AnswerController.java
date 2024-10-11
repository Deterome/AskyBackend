package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/answers")
public class AnswerController implements DefaultControllerInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    @Autowired
    private AnswerService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AnswerResponseDto> getAllElements() {
        return service.findAllElements();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto getElementById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.findElementById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AnswerResponseDto addElement(@NonNull @RequestBody AnswerCreateDto element) {
        return service.addElement(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto updateElement(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody AnswerCreateDto updatedElement) {
        return service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteElement(@NonNull @PathVariable(name = "id") UUID id) {
        service.deleteElement(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto findAnswerByParams(@RequestParam(name = "author", required = false) String authorName,
                                                @RequestParam(name = "question_id", required = false) UUID questionId,
                                                @RequestParam(name = "body", required = false) String body) {
        return service.findAnswerByParams(authorName, questionId, body);
    }
}
