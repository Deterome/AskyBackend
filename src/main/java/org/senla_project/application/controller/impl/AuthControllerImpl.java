package org.senla_project.application.controller.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.controller.AuthController;
import org.senla_project.application.dto.jwt.JwtRequest;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    final private AuthService authService;

    @Override
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String authorizeAndGetAuthToken(@NonNull @RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @Override
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createNewUser(@NonNull @RequestBody UserCreateDto userCreateDto) {
        return authService.createNewUser(userCreateDto);
    }

}
