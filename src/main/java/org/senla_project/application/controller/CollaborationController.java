package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.service.CollaborationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/collabs")
@RequiredArgsConstructor
public class CollaborationController implements DefaultControllerInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    final private CollaborationService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<CollaborationResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") int pageNumber, @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return service.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public CollaborationResponseDto create(@NonNull @RequestBody CollaborationCreateDto element) {
        return service.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CollaborationResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody CollaborationCreateDto updatedElement) {
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
    public CollaborationResponseDto getByCollabName(@NonNull @RequestParam(name = "collab_name") String collabName) {
        return service.getByCollabName(collabName);
    }

}