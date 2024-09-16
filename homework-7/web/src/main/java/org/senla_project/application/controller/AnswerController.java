package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.service.AnswerService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AnswerController implements ControllerInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    @Autowired
    private AnswerService service;

    @Override
    @GetMapping("/answers/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AnswerResponseDto> getAllElements() {
        var elements = service.getAllElements();
        if (elements.isEmpty()) throw new EntityNotFoundException("Answers not found");
        return elements;
    }

    @Override
    public AnswerResponseDto findElementById(@NonNull UUID id) {
        return service.findElementById(id).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Override
    @PostMapping("/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public void addElement(@NonNull @RequestBody AnswerCreateDto element) {
        service.addElement(element);
    }

    @Override
    @PutMapping("/answers")
    @ResponseStatus(HttpStatus.OK)
    public void updateElement(@NonNull @RequestParam UUID id, @NonNull @RequestBody AnswerCreateDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/answers")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElement(@NonNull @RequestParam UUID id) {
        service.deleteElement(id);
    }

    public AnswerResponseDto findAnswerByParams(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body) {
        return service.findAnswer(authorName, questionId, body).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @GetMapping("/answers")
    @ResponseStatus(HttpStatus.OK)
    public AnswerResponseDto findAnswer(@RequestParam(name = "id", required = false) UUID answerId,
                                             @RequestParam(name ="author", required = false) String authorName,
                                             @RequestParam(name ="question-id", required = false) UUID questionId,
                                             @RequestParam(name ="body", required = false) String body) {
        if (answerId != null)
            return findElementById(answerId);
        if (authorName != null && questionId != null && body != null)
            return findAnswerByParams(authorName, questionId, body);
        throw new InvalidRequestParametersException("Invalid requests parameters");
    }
}
