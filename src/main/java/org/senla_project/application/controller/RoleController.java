package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleResponseDto;
import org.senla_project.application.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController implements CrudController<RoleCreateDto, RoleResponseDto, RoleDeleteDto, UUID> {

    final private RoleService roleService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<RoleResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") @Positive @Min(1) int pageNumber, @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize) {
        return roleService.getAll(PageRequest.of(pageNumber - 1, pageSize));
    }

    @Override
    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return roleService.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public RoleResponseDto create(@NonNull @RequestBody RoleCreateDto element) {
        return roleService.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody RoleCreateDto updatedElement) {
        return roleService.updateById(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody RoleDeleteDto roleDeleteDto) {
        roleService.delete(roleDeleteDto);
    }

    @GetMapping("/{role_name}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto getByRoleName(@NonNull @PathVariable(name = "role_name") String roleName) {
        return roleService.getByRoleName(roleName);
    }
}
