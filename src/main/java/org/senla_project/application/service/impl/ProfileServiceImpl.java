package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.service.ProfileService;
import org.senla_project.application.service.linker.ProfileLinkerService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.ForbiddenException;
import org.senla_project.application.util.security.AuthenticationManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
        profile.setUser(User.builder().username(AuthenticationManager.getUsernameOfAuthenticatedUser()).build());
        profileLinkerService.linkProfileWithUser(profile);
        return profileMapper.toProfileResponseDto(profileRepository.save(profile));
    }

    @Transactional
    @Override
    public ProfileResponseDto update(@NonNull ProfileUpdateDto profileUpdateDto) throws EntityNotFoundException {
        Optional<Profile> oldProfile = profileRepository.findById(UUID.fromString(profileUpdateDto.getProfileId()));
        if (oldProfile.isEmpty()) throw new EntityNotFoundException("Profile not found");
        if (AuthenticationManager.ifUsernameBelongsToAuthenticatedUser(oldProfile.get().getUser().getUsername())
                || AuthenticationManager.isAuthenticatedUserAnAdmin()) {
            Profile profile = profileMapper.toProfile(profileUpdateDto);
            profile.setUser(oldProfile.get().getUser());

            return profileMapper.toProfileResponseDto(profileRepository.save(profile));
        } else {
            throw new ForbiddenException(String.format("Profile of user %s is not yours! You can't update this profile!", oldProfile.get().getUser().getUsername()));
        }

    }

    @Transactional
    @Override
    @PreAuthorize("#deletedProfile.username == authentication.principal.username || hasAuthority('admin')")
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
