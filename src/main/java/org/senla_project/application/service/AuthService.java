package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.jwt.JwtRequest;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;

public interface AuthService {
    String createAuthToken(@NonNull JwtRequest authRequest);
    UserResponseDto createNewUser(UserCreateDto userCreateDto);
}
