package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    final private UserService service;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDto> getAllElements(@RequestParam(name="page") int pageNumber) {
        return service.findAllElements(pageNumber);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getElementById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.findElementById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto updateElement(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody UserCreateDto updatedElement) {
        return service.updateElement(id, updatedElement);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteElement(@NonNull @PathVariable(name = "id") UUID id) {
        service.deleteElement(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto findUserByName(@NonNull @RequestParam(name = "username") String username) {
        return service.findUserByUsername(username);
    }
}
