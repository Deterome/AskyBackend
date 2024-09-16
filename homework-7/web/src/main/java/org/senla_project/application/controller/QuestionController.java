package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.service.QuestionService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class QuestionController implements ControllerInterface<UUID, QuestionCreateDto, QuestionResponseDto> {

    @Autowired
    private QuestionService service;

    @Override
    @GetMapping("/questions/all")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionResponseDto> getAllElements() {
        var elements = service.getAllElements();
        if (elements.isEmpty()) throw new EntityNotFoundException("Questions not found");
        return elements;
    }

    @Override
    public QuestionResponseDto findElementById(@NonNull UUID id) {
        return service.findElementById(id).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Override
    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    public void addElement(@NonNull @RequestBody QuestionCreateDto element) {
        service.addElement(element);
    }

    @Override
    @PutMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    public void updateElement(@NonNull @RequestParam UUID id, @NonNull @RequestBody QuestionCreateDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElement(@NonNull @RequestParam UUID id) {
        service.deleteElement(id);
    }

    public QuestionResponseDto findQuestionByParams(@NonNull String header, @NonNull String body, @NonNull String authorName) {
        return service.findQuestion(header, body, authorName).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @GetMapping("/questions")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto findQuestion(@RequestParam(name = "id", required = false) UUID questionId,
                                    @RequestParam(name = "header", required = false) String header,
                                    @RequestParam(name = "body", required = false) String body,
                                    @RequestParam(name = "author", required = false) String authorName) {
        if (questionId != null)
            return findElementById(questionId);
        if (header != null && body != null && authorName != null)
            return findQuestionByParams(header, body, authorName);
        throw new InvalidRequestParametersException("Invalid requests parameters");
    }
}
