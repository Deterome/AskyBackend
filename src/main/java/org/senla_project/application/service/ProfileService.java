package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.CollaborationsJoiningResponseDto;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService implements ServiceInterface<UUID, ProfileCreateDto, ProfileResponseDto> {

    final private ProfileRepository profileRepository;
    final private ProfileMapper profileMapper;
    final private UserService userService;
    final private UserMapper userMapper;

    @Transactional
    @Override
    public ProfileResponseDto create(@NonNull ProfileCreateDto element) {
        return profileMapper.toProfileResponseDto(profileRepository.save(
                addDependenciesProfile(profileMapper.toProfile(element))
        ));
    }

    @Transactional
    @Override
    public ProfileResponseDto updateById(@NonNull UUID id, @NonNull ProfileCreateDto updatedElement) throws EntityNotFoundException {
        if (!profileRepository.existsById(id)) throw new EntityNotFoundException("Profile not found");
        return profileMapper.toProfileResponseDto(profileRepository.save(
                addDependenciesProfile(profileMapper.toProfile(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        profileRepository.deleteById(id);
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
    public ProfileResponseDto getByUsername(String nickname) throws EntityNotFoundException {
        return profileRepository.findByUsername(nickname)
                .map(profileMapper::toProfileResponseDto).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    @Transactional(readOnly = true)
    public Profile addDependenciesProfile(Profile profile) {
        profile.setUser(userMapper.toUser(
                userService.getByUsername(profile.getUser().getUsername())
        ));

        return profile;
    }
}
