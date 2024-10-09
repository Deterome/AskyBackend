package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.JwtRequest;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String createAuthToken(@NonNull @RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createNewUser(@NonNull @RequestBody UserCreateDto userCreateDto) {
        return authService.createNewUser(userCreateDto);
    }

}
