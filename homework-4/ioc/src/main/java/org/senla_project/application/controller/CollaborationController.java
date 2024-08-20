package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.service.CollaborationService;
import org.senla_project.application.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CollaborationController implements ControllerInterface<CollaborationDto> {

    @Autowired
    private CollaborationService service;
    @Autowired
    private JsonParser jsonParser;

    @Override
    public void execute() {
        service.execute();
    }

    @Override
    public String getAllElements() {
        return jsonParser.parseObjectToJson(service.getAllElements());
    }

    @Override
    public String getElementById(@NonNull UUID id) {
        return jsonParser.parseObjectToJson(service.getElementById(id));
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
    public void deleteElement(@NonNull UUID id) {
        service.deleteElement(id);
    }

    public UUID findCollabId(String collabName) {
        return service.findCollabId(collabName);
    }

}