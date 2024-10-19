package org.senla_project.application.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController implements DefaultControllerInterface<UUID, RoleCreateDto, RoleResponseDto> {

    final private RoleService service;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<RoleResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") int pageNumber, @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize) {
        return service.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return service.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@NonNull @RequestBody RoleCreateDto element) {
        return service.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody RoleCreateDto updatedElement) {
        return service.updateById(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @PathVariable(name = "id") UUID id) {
        service.deleteById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto getByRoleName(@NonNull @RequestParam(name = "role_name") String roleName) {
        return service.getByRoleName(roleName);
    }
}
