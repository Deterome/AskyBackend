package org.senla_project.application.controller.impl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.controller.CollaborationController;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningResponseDto;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabDeleteDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.dto.collaboration.CollabUpdateDto;
import org.senla_project.application.service.CollaborationService;
import org.senla_project.application.service.CollaborationsJoiningService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/collabs")
@RequiredArgsConstructor
public class CollaborationControllerImpl implements CollaborationController {

    final private CollaborationService collabService;
    final private CollaborationsJoiningService collabJoinService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<CollabResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") @Positive @Min(1) int pageNumber, @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize) {
        return collabService.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CollabResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return collabService.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CollabResponseDto create(@NonNull @RequestBody CollabCreateDto element) {
        return collabService.create(element);
    }

    @Override
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public CollabResponseDto update(@NonNull @RequestBody CollabUpdateDto collabUpdateDto) {
        return collabService.update(collabUpdateDto);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody CollabDeleteDto collabDeleteDto) {
        collabService.delete(collabDeleteDto);
    }

    @Override
    @GetMapping("/{collab_name}")
    @ResponseStatus(HttpStatus.OK)
    public CollabResponseDto getByCollabName(@NonNull @PathVariable(name = "collab_name") String collabName) {
        return collabService.getByCollabName(collabName);
    }

    @Override
    @PostMapping("/{collab_name}/join")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationsJoiningResponseDto joinToCollab(@NonNull @PathVariable(name = "collab_name") String collabName) {
        return collabJoinService.joinAuthenticatedUserToCollab(collabName);
    }

}