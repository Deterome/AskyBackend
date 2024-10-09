package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profiles")
public class ProfileController implements ControllerInterface<UUID, ProfileCreateDto, ProfileResponseDto> {

    @Autowired
    private ProfileService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileResponseDto> getAllElements() {
        return service.findAllElements();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto getElementById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.findElementById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponseDto addElement(@NonNull @RequestBody ProfileCreateDto element) {
        return service.addElement(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto updateElement(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody ProfileCreateDto updatedElement) {
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
    public ProfileResponseDto findProfileByUsername(@NonNull @RequestParam(name = "username") String username) {
        return service.findProfileByUsername(username);
    }
}
