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

import static org.junit.jupiter.api.Assertions.*;
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
    UserController userController;

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

        userController.addElement(TestData.getUserCreateDto());
        profileController.addElement(TestData.getProfileCreateDto());
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/profiles?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileController.addElement(profileCreateDto);
        String profileIdString = profileController.findProfileByUsername(profileCreateDto.getUserName()).getProfileId();
        mockMvc.perform(get("/profiles?id={id}", profileIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(profileController.findElementById(UUID.fromString(profileIdString)).getProfileId(),
                profileIdString);
    }

    @Test
    void addElement() throws Exception {
        userController.addElement(TestData.getUserCreateDto());
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
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(put("/profiles/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        mockMvc.perform(put("/profiles/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        profileController.addElement(profileCreateDto);
        ProfileResponseDto profileResponseDto = profileController.findProfileByUsername(profileCreateDto.getUserName());
        ProfileCreateDto updatedProfileCreateDto = TestData.getUpdatedProfileCreateDto();

        mockMvc.perform(put("/profiles/update?id={id}", profileResponseDto.getProfileId())
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
        mockMvc.perform(delete("/profiles/delete?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        userController.addElement(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileController.addElement(profileCreateDto);
        String profileIdString = profileController.findProfileByUsername(profileCreateDto.getUserName()).getProfileId();
        mockMvc.perform(delete("/profiles/delete?id={id}", profileIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertThrows(EntityNotFoundException.class, () -> profileController.findElementById(UUID.fromString(profileIdString)));
    }

    @Test
    void findProfileByName() throws Exception {
        mockMvc.perform(get("/profiles?username={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileController.addElement(profileCreateDto);
        mockMvc.perform(get("/profiles?username={name}", profileCreateDto.getUserName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(profileController.findProfileByUsername(profileCreateDto.getUserName()).getUserName(),
                profileCreateDto.getUserName());
    }

    @Test
    void findProfile() throws Exception {
        mockMvc.perform(get("/profiles?username={name}&id={id}", "Alex", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileController.addElement(profileCreateDto);
        String profileIdString = profileController.findProfileByUsername(profileCreateDto.getUserName()).getProfileId();
        mockMvc.perform(get("/profiles?username={name}&id={id}", profileCreateDto.getUserName(), profileIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(profileController.findProfileByUsername(profileCreateDto.getUserName()).getUserName(),
                profileCreateDto.getUserName());

        mockMvc.perform(get("/profiles?username={name}&id={id}", profileCreateDto.getUserName(), UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/profiles?username={name}&id={id}", "Nick", profileIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}