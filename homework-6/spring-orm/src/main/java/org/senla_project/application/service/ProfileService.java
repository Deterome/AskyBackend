package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileService implements ServiceInterface<UUID, ProfileCreateDto, ProfileResponseDto> {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull ProfileCreateDto element) {
        profileRepository.create(profileMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull ProfileCreateDto updatedElement) {
        profileRepository.update(profileMapper.toEntity(id, updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        profileRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<ProfileResponseDto> getAllElements() {
        return profileMapper.toDtoList(profileRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<ProfileResponseDto> getElementById(@NonNull UUID id) {
        return profileRepository.findById(id)
                .map(profileMapper::toResponseDto);
    }

    @Transactional
    public Optional<UUID> getProfileId(String nickname) {
        return profileRepository.findProfileByNickname(nickname).map(Profile::getProfileId);
    }

}
