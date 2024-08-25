package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.ProfileDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileService implements ServiceInterface<ProfileDto, ProfileDto> {

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public void execute() {}

    @Override
    public List<ProfileDto> getAllElements() {
        return profileMapper.toDtoList(profileDao.findAll());
    }

    @Override
    public Optional<ProfileDto> getElementById(@NonNull UUID id) {
        return profileDao.findById(id)
                .map(profileMapper::toDto);
    }

    @Override
    public void addElement(@NonNull ProfileDto element) {
        profileDao.create(profileMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull ProfileDto updatedElement) {
        profileDao.update(id, profileMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        profileDao.deleteById(id);
    }

    public Optional<UUID> getProfileId(String nickname) {
        return profileDao.findProfileByNickname(nickname).map(Entity::getId);
    }

}
