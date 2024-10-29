package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface UserService extends CrudService<UserCreateDto, UserResponseDto, UserDeleteDto, UUID>, UserDetailsService {
    UserResponseDto getByUsername(@NonNull String username);
    boolean existsByUsername(String username);
}
