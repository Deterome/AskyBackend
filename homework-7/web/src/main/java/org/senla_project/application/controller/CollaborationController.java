package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.CollaborationService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CollaborationController implements ControllerInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    @Autowired
    private CollaborationService service;

    @Override
    @GetMapping("/collabs/all")
    @ResponseStatus(HttpStatus.OK)
    public List<CollaborationResponseDto> getAllElements() {
        var elements = service.getAllElements();
        if (elements.isEmpty()) throw new EntityNotFoundException("Collaborations not found");
        return elements;
    }

    @Override
    public CollaborationResponseDto findElementById(@NonNull UUID id) {
        return service.findElementById(id).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Override
    @PostMapping("/collabs/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addElement(@NonNull @RequestBody CollaborationCreateDto element) {
        service.addElement(element);
    }

    @Override
    @PutMapping("/collabs/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateElement(@NonNull @RequestParam(name = "id") UUID id, @NonNull @RequestBody CollaborationCreateDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/collabs/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElement(@NonNull @RequestParam(name = "id") UUID id) {
        service.deleteElement(id);
    }

    public CollaborationResponseDto findCollabByName(@NonNull String collabName) {
        return service.findCollab(collabName).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @GetMapping("/collabs")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationResponseDto findCollab(@RequestParam(name = "id", required = false) UUID collabId,
                                       @RequestParam(name = "collab-name", required = false) String collabName) {
        if (collabId != null && collabName != null) {
            CollaborationResponseDto responseDto = findElementById(collabId);
            if (!collabName.equals(responseDto.getCollabName()))
                throw new EntityNotFoundException("Collab with the specified parameters was not found");
            return responseDto;
        } else if (collabId != null) {
            return findElementById(collabId);
        } else if (collabName != null) {
            return findCollabByName(collabName);
        } else {
            throw new InvalidRequestParametersException("Invalid requests parameters");
        }
    }

}