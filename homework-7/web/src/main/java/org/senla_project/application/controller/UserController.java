package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController implements ControllerInterface<UUID, UserCreateDto, UserResponseDto> {

    @Autowired
    private UserService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllElements() {
        return service.getAllElements();
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getElementById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.findElementById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto addElement(@NonNull @RequestBody UserCreateDto element) {
        return service.addElement(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateElement(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody UserCreateDto updatedElement) {
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
    public UserResponseDto findUserByName(@RequestParam(name = "username", required = false) String username) {
        return service.findUserByName(username);
    }
}
