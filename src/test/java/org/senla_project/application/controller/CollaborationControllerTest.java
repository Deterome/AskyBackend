package org.senla_project.application.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.config.WebSecurityConfig;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
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
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class CollaborationControllerTest {

    final JsonParser jsonParser;
    final CollaborationController collabController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void getAll_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenReturnAllElements() throws Exception {
        collabController.create(TestData.getCollaborationCreateDto());
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.getAll(1, 5).getTotalElements(), 1);
    }

    @Test
    void getById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/collabs/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/collabs/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenReturnElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollaborationResponseDto createdCollaboration = collabController.create(collabCreateDto);
        mockMvc.perform(get("/collabs/{id}", createdCollaboration.getCollabId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdCollaboration.getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void create_thenThrowUnauthorizedException() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(post("/collabs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void create_thenReturnCreatedElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(post("/collabs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(collabController.getByCollabName(collabCreateDto.getCollabName()).getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(put("/collabs/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(put("/collabs/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollaborationResponseDto collabResponseDto = collabController.create(collabCreateDto);
        CollaborationCreateDto updatedCollaborationCreateDto = TestData.getUpdatedCollaborationCreateDto();

        mockMvc.perform(put("/collabs/update/{id}", collabResponseDto.getCollabId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedCollaborationCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.getByCollabName(updatedCollaborationCreateDto.getCollabName()).getCollabName(),
                updatedCollaborationCreateDto.getCollabName());
        Assertions.assertEquals(collabController.getByCollabName(updatedCollaborationCreateDto.getCollabName()).getCollabId(),
                collabResponseDto.getCollabId());
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/collabs/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void delete_thenDeleteElement() throws Exception {
        mockMvc.perform(delete("/collabs/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollaborationResponseDto collabResponseDto = collabController.create(collabCreateDto);
        mockMvc.perform(delete("/collabs/delete/{id}", collabResponseDto.getCollabId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> collabController.getById(UUID.fromString(collabResponseDto.getCollabId())));
    }

    @Test
    void getByCollabName_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/collabs?collab_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByCollabName_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/collabs?collab_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByCollabName_thenReturnElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.create(collabCreateDto);
        mockMvc.perform(get("/collabs?collab_name={name}", collabCreateDto.getCollabName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        CollaborationResponseDto collabResponseDto = collabController.getByCollabName(collabCreateDto.getCollabName());
        Assertions.assertEquals(collabResponseDto.getCollabName(),
                collabCreateDto.getCollabName());
    }

}