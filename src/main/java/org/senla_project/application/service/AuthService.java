package org.senla_project.application.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.senla_project.application.dto.JwtRequest;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.util.exception.AuthenticationException;
import org.senla_project.application.util.exception.RegistrationException;
import org.senla_project.application.util.securityUtil.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
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
    public UserResponseDto createNewUser(UserCreateDto userCreateDto) {
        if (userService.isUserExist(userCreateDto.getUsername())) {
            log.debug("User already exist");
            throw new RegistrationException("User with that name already exists!");
        } else {
            log.debug("Starting to create user from userService");
            return userService.addElement(userCreateDto);
        }
    }

}
