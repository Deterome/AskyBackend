package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.UserService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController implements ControllerInterface<UUID, UserCreateDto, UserResponseDto> {

    @Autowired
    private UserService service;

    @Override
    @GetMapping("/users/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllElements() {
        var elements = service.getAllElements();
        if (elements.isEmpty()) throw new EntityNotFoundException("Users not found");
        return elements;
    }

    @Override
    public UserResponseDto findElementById(@NonNull UUID id) {
        return service.findElementById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @PostMapping("/users/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addElement(@NonNull @RequestBody UserCreateDto element) {
        service.addElement(element);
    }

    @Override
    @PutMapping("/users/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateElement(@NonNull @RequestParam(name = "id") UUID id, @NonNull @RequestBody UserCreateDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/users/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElement(@NonNull @RequestParam(name = "id") UUID id) {
        service.deleteElement(id);
    }

    public UserResponseDto findUserByName(@NonNull String username) {
        return service.findUser(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto findUser(@RequestParam(name = "id", required = false) UUID userId,
                                    @RequestParam(name = "username", required = false) String username) {
        if (userId != null && username != null) {
            UserResponseDto responseDto = findElementById(userId);
            if (!username.equals(responseDto.getNickname()))
                throw new EntityNotFoundException("User with the specified parameters was not found");
            return responseDto;
        } else if (userId != null) {
            return findElementById(userId);
        } else if (username != null) {
            return findUserByName(username);
        } else {
            throw new InvalidRequestParametersException("Invalid requests parameters");
        }
    }
}
