package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.jwt.JwtRequest;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;

public interface AuthController {

    String authorizeAndGetAuthToken(@NonNull JwtRequest authRequest);

    UserResponseDto createNewUser(@NonNull UserCreateDto userCreateDto);

}
