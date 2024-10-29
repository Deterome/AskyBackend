package org.senla_project.application.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.service.ProfileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController implements CrudController<ProfileCreateDto, ProfileResponseDto, ProfileDeleteDto, UUID> {

    final private ProfileService profileService;

    @Override
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProfileResponseDto> getAll(@RequestParam(name = "page", defaultValue = "1") @Positive @Min(1) int pageNumber, @RequestParam(name = "page_size", defaultValue = "10") @Positive @Min(1) int pageSize) {
        return profileService.getAll(PageRequest.of(pageNumber - 1, pageSize));
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
    public ProfileResponseDto create(@NonNull @RequestBody ProfileCreateDto element) {
        return profileService.create(element);
    }

    @Override
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto update(@NonNull @PathVariable(name = "id") UUID id, @NonNull @RequestBody ProfileCreateDto updatedElement) {
        return profileService.updateById(id, updatedElement);
    }

    @Override
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@NonNull @RequestBody ProfileDeleteDto profileDeleteDto) {
        profileService.delete(profileDeleteDto);
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ProfileResponseDto getByUsername(@NonNull @PathVariable(name = "username") String username) {
        return profileService.getByUsername(username);
    }
}
