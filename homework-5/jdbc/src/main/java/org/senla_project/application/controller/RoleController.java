package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.service.RoleService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RoleController implements ControllerInterface<RoleDto> {

    @Autowired
    private RoleService service;
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
        return jsonParser.parseObjectToJson(service.getElementById(id).orElseThrow(() -> new EntityNotFoundException("Role not found")));
    }

    @Override
    public void addElement(@NonNull RoleDto element) {
        service.addElement(element);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull RoleDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        service.deleteElement(id);
    }

    public Optional<UUID> findRoleId(String roleName) {
        return service.findRoleId(roleName);
    }
}
