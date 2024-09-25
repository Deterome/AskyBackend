package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.ProfileService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProfileController implements ControllerInterface<UUID, ProfileCreateDto, ProfileResponseDto> {

    @Autowired
    private ProfileService service;

    @Override
    @GetMapping("/profiles/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProfileResponseDto> getAllElements() {
        var elements = service.getAllElements();
        if (elements.isEmpty()) throw new EntityNotFoundException("Profiles not found");
        return elements;
    }

    @Override
    public ProfileResponseDto findElementById(@NonNull UUID id) {
        return service.findElementById(id).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    @Override
    @PostMapping("/profiles/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addElement(@NonNull @RequestBody ProfileCreateDto element) {
        service.addElement(element);
    }

    @Override
    @PutMapping("/profiles/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateElement(@NonNull @RequestParam(name = "id") UUID id, @NonNull @RequestBody ProfileCreateDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/profiles/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElement(@NonNull @RequestParam(name = "id") UUID id) {
        service.deleteElement(id);
    }

    public ProfileResponseDto findProfileByUsername(@NonNull String username) {
        return service.findProfile(username).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    @GetMapping("/profiles")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto findProfile(@RequestParam(name = "id", required = false) UUID profileId,
                                    @RequestParam(name = "username", required = false) String username) {
        if (profileId != null && username != null) {
            ProfileResponseDto responseDto = findElementById(profileId);
            if (!username.equals(responseDto.getUserName()))
                throw new EntityNotFoundException("Profile with the specified parameters was not found");
            return responseDto;
        } else if (profileId != null) {
            return findElementById(profileId);
        } else if (username != null) {
            return findProfileByUsername(username);
        } else {
            throw new InvalidRequestParametersException("Invalid requests parameters");
        }
    }
}
