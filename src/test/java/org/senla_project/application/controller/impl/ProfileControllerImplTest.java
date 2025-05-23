package org.senla_project.application.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.*;
import org.senla_project.application.dto.profile.ProfileCreateDto;
import org.senla_project.application.dto.profile.ProfileDeleteDto;
import org.senla_project.application.dto.profile.ProfileResponseDto;
import org.senla_project.application.dto.profile.ProfileUpdateDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.sort.ProfileSortType;
import org.senla_project.application.util.sort.SortOrder;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, WebConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class ProfileControllerImplTest {

    final JsonParser jsonParser;
    final ProfileControllerImpl profileController;
    final RoleControllerImpl roleController;
    final AuthControllerImpl authController;

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
        roleController.create(TestData.getRoleCreateDto());
        authController.createNewUser(TestData.getUserCreateDto());
    }

    @Test
    void getAll_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenThrowNotFoundException() throws Exception {
        profileController.create(TestData.getProfileCreateDto());
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(profileController.getAll(1, 5, ProfileSortType.USERNAME, SortOrder.ASCENDING).getTotalElements(), 1);
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenReturnAllElements() throws Exception {
        profileController.create(TestData.getProfileCreateDto());
        mockMvc.perform(get("/profiles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(1));
    }

    @Test
    void getById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/profiles/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/profiles/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenReturnElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto createdProfile = profileController.create(profileCreateDto);
        mockMvc.perform(get("/profiles/id/{id}", createdProfile.getProfileId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(createdProfile.getUsername()));
    }

    @Test
    void create_thenReturnUnauthorized() throws Exception {
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
    void create_thenReturnCreatedElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        mockMvc.perform(post("/profiles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value(TestData.AUTHORIZED_USER_NAME));
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(put("/profiles/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        ProfileUpdateDto profileUpdateDto = TestData.getProfileUpdateDto();
        profileUpdateDto.setProfileId(UUID.randomUUID().toString());
        mockMvc.perform(put("/profiles/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileUpdateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto profileResponseDto = profileController.create(profileCreateDto);
        ProfileUpdateDto profileUpdateDto = TestData.getProfileUpdateDto();
        profileUpdateDto.setProfileId(profileResponseDto.getProfileId());

        mockMvc.perform(put("/profiles/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(TestData.AUTHORIZED_USER_NAME))
                .andExpect(jsonPath("firstname").value(profileUpdateDto.getFirstname()))
                .andExpect(jsonPath("profileId").value(profileResponseDto.getProfileId()));
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        ProfileDeleteDto profileDeleteDto = ProfileDeleteDto.builder()
                .username("Alex")
                .build();
        mockMvc.perform(delete("/profiles/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void delete_thenDeleteElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto profileResponseDto = profileController.create(profileCreateDto);
        ProfileDeleteDto profileDeleteDto = ProfileDeleteDto.builder()
                .username(profileResponseDto.getUsername())
                .build();
        mockMvc.perform(delete("/profiles/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(profileDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getByUsername_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/profiles/{username}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByUsername_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/profiles/{username}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByUsername_thenReturnElement() throws Exception {
        ProfileCreateDto profileCreateDto = TestData.getProfileCreateDto();
        ProfileResponseDto profileResponseDto = profileController.create(profileCreateDto);
        mockMvc.perform(get("/profiles/{username}", profileResponseDto.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(profileResponseDto.getUsername()));
    }

}