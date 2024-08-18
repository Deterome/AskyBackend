package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.ProfileDao;
import org.senla_project.application.db.dao.QuestionDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dto.ProfileDto;
import org.senla_project.application.db.entities.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProfileService implements ServiceInterface<ProfileDto> {

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<ProfileDto> getAllElements() {
        return profileDao.findAll().stream().map(ProfileDto::new).toList();
    }

    @Override
    @Nullable
    public ProfileDto getElementById(@NonNull UUID id) {
        var profile = profileDao.findById(id);
        if (profile == null) return null;
        return new ProfileDto(profile);
    }

    @Override
    public void addElement(@NonNull ProfileDto element) {
        Profile newElement = Profile.builder()
                .bio(element.getBio())
                .user(userDao.findUserByNickname(element.getUserName()))
                .avatarUrl(element.getAvatarUrl())
                .birthday(element.getBirthday())
                .firstname(element.getFirstname())
                .surname(element.getSurname())
                .rating(element.getRating())
            .build();
        profileDao.create(newElement);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull ProfileDto updatedElement) {
        Profile updatedProfile = Profile.builder()
                .bio(updatedElement.getBio())
                .user(userDao.findUserByNickname(updatedElement.getUserName()))
                .avatarUrl(updatedElement.getAvatarUrl())
                .birthday(updatedElement.getBirthday())
                .firstname(updatedElement.getFirstname())
                .surname(updatedElement.getSurname())
                .rating(updatedElement.getRating())
            .build();
        updatedProfile.setId(updatedElement.getProfileId());
        profileDao.update(id, updatedProfile);
    }

    @Override
    public void deleteElement(@NonNull ProfileDto element) {
        profileDao.deleteById(element.getProfileId());
    }

}
