package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class ProfileControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    ProfileController profileController;
    @Autowired
    AuthController authController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        authController.createNewUser(TestData.getUserCreateDto());
        profileController.addElement(TestData.getProfileCreateDto());
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/profiles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        authController.createNewUser(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto createdProfile = profileController.addElement(profileCreateDto);
        mockMvc.perform(get("/profiles/{id}", createdProfile.getProfileId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdProfile.getUserName(),
                profileCreateDto.getUserName());
    }

    @Test
    void addElement() throws Exception {
        authController.createNewUser(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(post("/profiles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(profileController.findProfileByUsername(profileCreateDto.getUserName()).getUserName(),
                profileCreateDto.getUserName());
    }

    @Test
    void updateElement() throws Exception {
        authController.createNewUser(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(put("/profiles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        ProfileResponseDto profileResponseDto = profileController.addElement(profileCreateDto);
        ProfileCreateDto updatedProfileCreateDto = TestData.getUpdatedProfileCreateDto();

        mockMvc.perform(put("/profiles/update/{id}", profileResponseDto.getProfileId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedProfileCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.findProfileByUsername(updatedProfileCreateDto.getUserName()).getUserName(),
                updatedProfileCreateDto.getUserName());
        Assertions.assertEquals(profileController.findProfileByUsername(updatedProfileCreateDto.getUserName()).getProfileId(),
                profileResponseDto.getProfileId());
    }

    @Test
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/profiles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        authController.createNewUser(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto profileResponseDto = profileController.addElement(profileCreateDto);
        mockMvc.perform(delete("/profiles/delete/{id}", profileResponseDto.getProfileId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> profileController.getElementById(UUID.fromString(profileResponseDto.getProfileId())));
    }

    @Test
    void findProfileByUsername() throws Exception {
        mockMvc.perform(get("/profiles?username={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        authController.createNewUser(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileController.addElement(profileCreateDto);
        mockMvc.perform(get("/profiles?username={name}", profileCreateDto.getUserName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        ProfileResponseDto profileResponseDto = profileController.findProfileByUsername(profileCreateDto.getUserName());
        Assertions.assertEquals(profileResponseDto.getUserName(),
                profileCreateDto.getUserName());
    }

}