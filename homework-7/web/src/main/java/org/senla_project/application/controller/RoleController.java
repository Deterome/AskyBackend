package org.senla_project.application.controller;

import lombok.NonNull;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.service.RoleService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class RoleController implements ControllerInterface<UUID, RoleCreateDto, RoleResponseDto> {

    @Autowired
    private RoleService service;

    @Override
    @GetMapping("/roles/all")
    @ResponseStatus(HttpStatus.OK)
    public List<RoleResponseDto> getAllElements() {
        var elements = service.getAllElements();
        if (elements.isEmpty()) throw new EntityNotFoundException("Roles not found");
        return elements;
    }

    @Override
    public RoleResponseDto findElementById(@NonNull UUID id) {
        return service.findElementById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    @PostMapping("/roles/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void addElement(@NonNull @RequestBody RoleCreateDto element) {
        service.addElement(element);
    }

    @Override
    @PutMapping("/roles/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateElement(@NonNull @RequestParam(name = "id") UUID id, @NonNull @RequestBody RoleCreateDto updatedElement) {
        service.updateElement(id, updatedElement);
    }

    @Override
    @DeleteMapping("/roles/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteElement(@NonNull @RequestParam(name = "id") UUID id) {
        service.deleteElement(id);
    }

    public RoleResponseDto findRoleByName(@NonNull String roleName) {
        return service.findRole(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @GetMapping("/roles")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto findRole(@RequestParam(name = "id", required = false) UUID roleId,
                                    @RequestParam(name = "role-name", required = false) String roleName) {
        if (roleId != null && roleName != null) {
            RoleResponseDto responseDto = findElementById(roleId);
            if (!roleName.equals(responseDto.getRoleName()))
                throw new EntityNotFoundException("Role with the specified parameters was not found");
            return responseDto;
        } else if (roleId != null) {
            return findElementById(roleId);
        } else if (roleName != null) {
            return findRoleByName(roleName);
        } else {
            throw new InvalidRequestParametersException("Invalid requests parameters");
        }
    }
}
