package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.config.WebSecurityConfig;
import org.senla_project.application.dto.ProfileCreateDto;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class ProfileControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    ProfileController profileController;
    @Autowired
    RoleController roleController;
    @Autowired
    AuthController authController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @BeforeEach
    void initDataBaseWithData() {
        roleController.addElement(TestData.getRoleCreateDto());
        authController.createNewUser(TestData.getUserCreateDto());
    }

    @Test
    void getAllElements_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAllElements_thenThrowNotFoundException() throws Exception {
        profileController.addElement(TestData.getProfileCreateDto());
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.getAllElements().size(), 1);
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAllElements_thenReturnAllElements() throws Exception {
        profileController.addElement(TestData.getProfileCreateDto());
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.getAllElements().size(), 1);
    }

    @Test
    void findElementById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/profiles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findElementById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/profiles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findElementById_thenReturnElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto createdProfile = profileController.addElement(profileCreateDto);
        mockMvc.perform(get("/profiles/{id}", createdProfile.getProfileId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdProfile.getUsername(),
                profileCreateDto.getUsername());
    }

    @Test
    void addElement_thenReturnUnauthorized() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(post("/profiles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void addElement_thenReturnCreatedElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(post("/profiles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(profileController.findProfileByUsername(profileCreateDto.getUsername()).getUsername(),
                profileCreateDto.getUsername());
    }

    @Test
    void updateElement_thenThrowUnauthorizedException() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(put("/profiles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void updateElement_thenThrowPreconditionFailedException() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(put("/profiles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void updateElement_thenReturnUpdatedElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto profileResponseDto = profileController.addElement(profileCreateDto);
        ProfileCreateDto updatedProfileCreateDto = TestData.getUpdatedProfileCreateDto();

        mockMvc.perform(put("/profiles/update/{id}", profileResponseDto.getProfileId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedProfileCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.findProfileByUsername(updatedProfileCreateDto.getUsername()).getUsername(),
                updatedProfileCreateDto.getUsername());
        Assertions.assertEquals(profileController.findProfileByUsername(updatedProfileCreateDto.getUsername()).getProfileId(),
                profileResponseDto.getProfileId());
    }

    @Test
    void deleteElement_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/profiles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void deleteElement_thenDeleteElement() throws Exception {
        mockMvc.perform(delete("/profiles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto profileResponseDto = profileController.addElement(profileCreateDto);
        mockMvc.perform(delete("/profiles/delete/{id}", profileResponseDto.getProfileId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> profileController.getElementById(UUID.fromString(profileResponseDto.getProfileId())));
    }

    @Test
    void findProfileByUsername_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/profiles?username={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findProfileByUsername_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/profiles?username={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findProfileByUsername_thenReturnElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        profileController.addElement(profileCreateDto);
        mockMvc.perform(get("/profiles?username={name}", profileCreateDto.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        ProfileResponseDto profileResponseDto = profileController.findProfileByUsername(profileCreateDto.getUsername());
        Assertions.assertEquals(profileResponseDto.getUsername(),
                profileCreateDto.getUsername());
    }

}