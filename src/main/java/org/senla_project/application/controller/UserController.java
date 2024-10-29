package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponseDto> getAll(@RequestParam(name = "page", defaultValue = "1") @Positive @Min(1) int pageNumber, @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize) {
        return userService.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return userService.getById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody UserCreateDto updatedElement) {
        return userService.updateById(id, updatedElement);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody UserDeleteDto userDeleteDto) {
        userService.delete(userDeleteDto);
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getByUsername(@NonNull @PathVariable(name = "username") String username) {
        return userService.getByUsername(username);
    }
}
