package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController implements DefaultControllerInterface<UUID, ProfileCreateDto, ProfileResponseDto> {

    final private ProfileService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProfileResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") int pageNumber, @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return service.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponseDto create(@NonNull @RequestBody ProfileCreateDto element) {
        return service.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody ProfileCreateDto updatedElement) {
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
    public ProfileResponseDto getByUsername(@NonNull @RequestParam(name = "username") String username) {
        return service.getByUsername(username);
    }
}
