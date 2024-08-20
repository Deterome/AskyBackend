package org.senla_project.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.senla_project.application.dto.UserDto;
import org.senla_project.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserController implements ControllerInterface<UserDto> {

    @Autowired
    private UserService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute() {
        service.execute();
    }

    @SneakyThrows
    @Override
    public String getAllElements() {
        return objectMapper.writeValueAsString(service.getAllElements());
    }

    @SneakyThrows
    @Override
    public String getElementById(@NonNull UUID id) {
        return objectMapper.writeValueAsString(service.getElementById(id));
    }

    @Override
    public void addElement(@NonNull UserDto element) {
        service.addElement(element);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull UserDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    public void deleteElement(@NonNull UserDto element) {
        service.deleteElement(element);
    }
}
