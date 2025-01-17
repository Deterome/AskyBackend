package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.senla_project.application.dto.jwt.JwtRequest;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.service.AuthService;
import org.senla_project.application.service.UserService;
import org.senla_project.application.util.exception.AuthenticationException;
import org.senla_project.application.util.exception.RegistrationException;
import org.senla_project.application.util.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    final private UserService userService;
    final private JwtUtil jwtUtil;
    final private AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    @Override
    public String createAuthToken(@NonNull JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid login or password!");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }

    @Transactional
    @Override
    public UserResponseDto createNewUser(UserCreateDto userCreateDto) {
        if (userService.existsByUsername(userCreateDto.getUsername())) {
            log.debug("User already exist");
            throw new RegistrationException("User with that name already exists!");
        } else {
            log.debug("Starting to create user from userService");
            return userService.create(userCreateDto);
        }
    }

}
