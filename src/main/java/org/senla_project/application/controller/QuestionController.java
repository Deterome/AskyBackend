package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
public class QuestionController implements ControllerInterface<UUID, QuestionCreateDto, QuestionResponseDto> {

    @Autowired
    private QuestionService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionResponseDto> getAllElements() {
        return service.getAllElements();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto getElementById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.findElementById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponseDto addElement(@NonNull @RequestBody QuestionCreateDto element) {
        return service.addElement(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuestionResponseDto updateElement(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody QuestionCreateDto updatedElement) {
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
    public QuestionResponseDto findQuestionByParams(@RequestParam(name = "header", required = false) String header,
                                                    @RequestParam(name = "body", required = false) String body,
                                                    @RequestParam(name = "author", required = false) String authorName) {
        return service.findQuestionByParams(header, body, authorName);
    }
}
