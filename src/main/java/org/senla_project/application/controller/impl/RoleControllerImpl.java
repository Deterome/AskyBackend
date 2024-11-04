package org.senla_project.application.controller.impl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.controller.RoleController;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleResponseDto;
import org.senla_project.application.dto.role.RoleUpdateDto;
import org.senla_project.application.service.RoleService;
import org.senla_project.application.util.sort.RoleSortType;
import org.senla_project.application.util.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {

    final private RoleService roleService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<RoleResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") @Positive @Min(1) int pageNumber,
                                        @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize,
                                        @RequestParam(name = "sort", defaultValue = "RoleName") RoleSortType sortType,
                                        @RequestParam(name = "order", defaultValue = "Ascending") SortOrder sortOrder) {
        return roleService.getAll(PageRequest.of(
                pageNumber - 1,
                pageSize,
                sortOrder.equals(SortOrder.ASCENDING) ?
                        Sort.by(sortType.getSortingFieldName()).ascending() :
                        Sort.by(sortType.getSortingFieldName()).descending()
        ));
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
    public RoleResponseDto create(@NonNull @RequestBody RoleCreateDto roleCreateDto) {
        return roleService.create(roleCreateDto);
    }

    @Override
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto update(@NonNull @RequestBody RoleUpdateDto roleUpdateDto) {
        return roleService.update(roleUpdateDto);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody RoleDeleteDto roleDeleteDto) {
        roleService.delete(roleDeleteDto);
    }

    @Override
    @GetMapping("/{role_name}")
    @ResponseStatus(HttpStatus.OK)
    public RoleResponseDto getByRoleName(@NonNull @PathVariable(name = "role_name") String roleName) {
        return roleService.getByRoleName(roleName);
    }
}
