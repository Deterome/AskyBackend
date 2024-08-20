package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.ProfileDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.mapper.CollaborationsJoiningListMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.mapper.ProfileListMapper;
import org.senla_project.application.mapper.ProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ProfileService implements ServiceInterface<ProfileDto> {

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private ProfileListMapper profileListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<ProfileDto> getAllElements() {
        return profileListMapper.toDtoList(profileDao.findAll());
    }

    @Override
    @Nullable
    public ProfileDto getElementById(@NonNull UUID id) {
        Profile profile = profileDao.findById(id);
        if (profile == null) return null;
        return profileMapper.toDto(profile);
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

    public UUID getProfileId(String nickname) {
        return profileDao.findProfileId(nickname);
    }

}
