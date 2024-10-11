package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        ProfileRepositoryImpl.class,
        UserRepositoryImpl.class
})
@Transactional
class ProfileRepositoryImplTest {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        userRepository.create(TestData.getUser());
    }

    Profile addDependenciesToProfile(Profile profile) {
        profile.setUser(userRepository.findUserByUsername(profile.getUser().getUsername()).get());
        return profile;
    }

    @Test
    void create() {
        Profile expectedProfile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.create(expectedProfile);
        Profile actual = profileRepository.findById(expectedProfile.getProfileId()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }

    @Test
    void findById() {
        Profile expectedProfile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.create(expectedProfile);
        Profile actual = profileRepository.findById(expectedProfile.getProfileId()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }

    @Test
    void findAll() {
        Profile profile = addDependenciesToProfile(TestData.getProfile());
        List<Profile> expectedProfileList = new ArrayList<>();
        expectedProfileList.add(profile);
        profileRepository.create(profile);
        List<Profile> actualProfileList = profileRepository.findAll();
        Assertions.assertEquals(expectedProfileList, actualProfileList);
    }

    @Test
    void update() {
        Profile profile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.create(profile);
        Profile expectedProfile = addDependenciesToProfile(TestData.getUpdatedProfile());
        expectedProfile.setProfileId(profile.getProfileId());
        profileRepository.update(expectedProfile);

        Profile actual = profileRepository.findById(expectedProfile.getProfileId()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }

    @Test
    void deleteById() {
        Profile profile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.create(profile);
        var profileId = profile.getProfileId();
        profileRepository.deleteById(profileId);
        Optional<Profile> actual = profileRepository.findById(profileId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findProfileByUsername() {
        Profile expectedProfile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.create(expectedProfile);
        Profile actual = profileRepository.findProfileByUsername(expectedProfile.getUser().getUsername()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }
}