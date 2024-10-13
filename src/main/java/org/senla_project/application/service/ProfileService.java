package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
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
    public ProfileResponseDto addElement(@NonNull ProfileCreateDto element) {
        return profileMapper.toProfileResponseDto(profileRepository.create(
                addDependenciesProfile(profileMapper.toProfile(element))
        ));
    }

    @Transactional
    @Override
    public ProfileResponseDto updateElement(@NonNull UUID id, @NonNull ProfileCreateDto updatedElement) {
        return profileMapper.toProfileResponseDto(profileRepository.update(
                addDependenciesProfile(profileMapper.toProfile(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        profileRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProfileResponseDto> findAllElements() throws EntityNotFoundException {
        var elements = profileMapper.toProfileDtoList(profileRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Profiles not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public ProfileResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return profileRepository.findById(id)
                .map(profileMapper::toProfileResponseDto).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    @Transactional(readOnly = true)
    public ProfileResponseDto findProfileByUsername(String nickname) throws EntityNotFoundException {
        return profileRepository.findProfileByUsername(nickname)
                .map(profileMapper::toProfileResponseDto).orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    @Transactional(readOnly = true)
    public Profile addDependenciesProfile(Profile profile) {
        profile.setUser(userMapper.toUser(
                userService.findUserByUsername(profile.getUser().getUsername())
        ));

        return profile;
    }
}
