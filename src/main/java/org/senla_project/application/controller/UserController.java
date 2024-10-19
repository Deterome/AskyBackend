package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<UserResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") int pageNumber, @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return service.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.getById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody UserCreateDto updatedElement) {
        return service.updateById(id, updatedElement);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable(name = "id") UUID id) {
        service.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getByUsername(@NonNull @RequestParam(name = "username") String username) {
        return service.getByUsername(username);
    }
}
