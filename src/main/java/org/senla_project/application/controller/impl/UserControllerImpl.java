package org.senla_project.application.controller.impl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.controller.UserController;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.service.UserService;
import org.senla_project.application.util.sort.SortOrder;
import org.senla_project.application.util.sort.UserSortType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    final private UserService userService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponseDto> getAll(@RequestParam(name = "page", defaultValue = "1") @Positive @Min(1) int pageNumber,
                                        @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize,
                                        @RequestParam(name = "sort", defaultValue = "Username") UserSortType sortType,
                                        @RequestParam(name = "order", defaultValue = "Ascending") SortOrder sortOrder) {
        return userService.getAll(PageRequest.of(
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
    public UserResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return userService.getById(id);
    }

    @Override
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto update(@NonNull @RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody UserDeleteDto userDeleteDto) {
        userService.delete(userDeleteDto);
    }

    @Override
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getByUsername(@NonNull @PathVariable(name = "username") String username) {
        return userService.getByUsername(username);
    }
}
