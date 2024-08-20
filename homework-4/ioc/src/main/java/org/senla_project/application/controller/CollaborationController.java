package org.senla_project.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.service.CollaborationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CollaborationController implements ControllerInterface<CollaborationDto> {

    @Autowired
    private CollaborationService service;
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
    public void addElement(@NonNull CollaborationDto element) {
        service.addElement(element);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    public void deleteElement(@NonNull CollaborationDto element) {
        service.deleteElement(element);
    }
}
