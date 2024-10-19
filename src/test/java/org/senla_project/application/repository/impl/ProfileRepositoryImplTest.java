package org.senla_project.application.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.repository.ProfileRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        ProfileRepository.class,
        UserRepository.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class ProfileRepositoryImplTest {

    final ProfileRepository profileRepository;
    final UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        userRepository.save(TestData.getUser());
    }

    Profile addDependenciesToProfile(Profile profile) {
        profile.setUser(userRepository.findByUsername(profile.getUser().getUsername()).get());
        return profile;
    }

    @Test
    void create() {
        Profile expectedProfile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.save(expectedProfile);
        Profile actual = profileRepository.findById(expectedProfile.getProfileId()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }

    @Test
    void findById() {
        Profile expectedProfile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.save(expectedProfile);
        Profile actual = profileRepository.findById(expectedProfile.getProfileId()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }

    @Test
    void findAll() {
        Profile profile = addDependenciesToProfile(TestData.getProfile());
        List<Profile> expectedProfileList = new ArrayList<>();
        expectedProfileList.add(profile);
        profileRepository.save(profile);
        List<Profile> actualProfileList = profileRepository.findAll();
        Assertions.assertEquals(expectedProfileList, actualProfileList);
    }

    @Test
    void update() {
        Profile profile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.save(profile);
        Profile expectedProfile = addDependenciesToProfile(TestData.getUpdatedProfile());
        expectedProfile.setProfileId(profile.getProfileId());
        profileRepository.save(expectedProfile);

        Profile actual = profileRepository.findById(expectedProfile.getProfileId()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }

    @Test
    void deleteById() {
        Profile profile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.save(profile);
        var profileId = profile.getProfileId();
        profileRepository.deleteById(profileId);
        Optional<Profile> actual = profileRepository.findById(profileId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByUsername() {
        Profile expectedProfile = addDependenciesToProfile(TestData.getProfile());
        profileRepository.save(expectedProfile);
        Profile actual = profileRepository.findByUsername(expectedProfile.getUser().getUsername()).get();
        Assertions.assertEquals(expectedProfile, actual);
    }
}