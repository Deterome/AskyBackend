package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.service.CrudService;
import org.senla_project.application.service.ProfileService;
import org.senla_project.application.service.UserService;
import org.senla_project.application.service.linker.ProfileLinkerService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    final private ProfileRepository profileRepository;
    final private ProfileMapper profileMapper;
    final private ProfileLinkerService profileLinkerService;

    @Transactional
    @Override
    public ProfileResponseDto create(@NonNull ProfileCreateDto element) {
        Profile profile = profileMapper.toProfile(element);
        profileLinkerService.linkProfileWithUser(profile);
        return profileMapper.toProfileResponseDto(profileRepository.save(profile));
    }

    @Transactional
    @Override
    @PreAuthorize("#updatedProfile.username == authentication.principal.username")
    public ProfileResponseDto update(@NonNull @P("updatedProfile") ProfileUpdateDto profileUpdateDto) throws EntityNotFoundException {
        if (!profileRepository.existsById(UUID.fromString(profileUpdateDto.getProfileId()))) throw new EntityNotFoundException("Profile not found");

        Profile profile = profileMapper.toProfile(profileUpdateDto);
        profileLinkerService.linkProfileWithUser(profile);
        return profileMapper.toProfileResponseDto(profileRepository.save(profile));
    }

    @Transactional
    @Override
    @PreAuthorize("#deletedProfile.username == authentication.principal.username")
    public void delete(@NonNull @P("deletedProfile") ProfileDeleteDto profileDeleteDto) {
        profileRepository.deleteByUsername(profileDeleteDto.getUsername());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProfileResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = profileRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Profile not found");
        return elements.map(profileMapper::toProfileResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return profileRepository.findById(id)
                .map(profileMapper::toProfileResponseDto).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileResponseDto getByUsername(String nickname) throws EntityNotFoundException {
        return profileRepository.findByUsername(nickname)
                .map(profileMapper::toProfileResponseDto).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

}
