package org.senla_project.application.controller.impl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.controller.ProfileController;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
import org.senla_project.application.service.ProfileService;
import org.senla_project.application.util.sort.ProfileSortType;
import org.senla_project.application.util.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileControllerImpl implements ProfileController {

    final private ProfileService profileService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProfileResponseDto> getAll(@RequestParam(name="page", defaultValue = "1") @Positive @Min(1) int pageNumber,
                                           @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize,
                                           @RequestParam(name = "sort", defaultValue = "Username") ProfileSortType sortType,
                                           @RequestParam(name = "order", defaultValue = "Ascending") SortOrder sortOrder) {
        return profileService.getAll(PageRequest.of(
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
    public ProfileResponseDto getById(@NonNull @PathVariable(name = "id") UUID id) {
        return profileService.getById(id);
    }

    @Override
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponseDto create(@NonNull @RequestBody ProfileCreateDto profileCreateDto) {
        return profileService.create(profileCreateDto);
    }

    @Override
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto update(@NonNull @RequestBody ProfileUpdateDto profileUpdateDto) {
        return profileService.update(profileUpdateDto);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody ProfileDeleteDto profileDeleteDto) {
        profileService.delete(profileDeleteDto);
    }

    @Override
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto getByUsername(@NonNull @PathVariable(name = "username") String username) {
        return profileService.getByUsername(username);
    }
}
