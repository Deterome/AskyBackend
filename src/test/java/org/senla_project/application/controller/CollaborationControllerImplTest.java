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
import org.senla_project.application.controller.impl.AuthControllerImpl;
import org.senla_project.application.controller.impl.CollaborationControllerImpl;
import org.senla_project.application.controller.impl.RoleControllerImpl;
import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabDeleteDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.service.CollabRoleService;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.enums.DefaultCollabRole;
import org.senla_project.application.util.enums.DefaultRole;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class CollaborationControllerImplTest {

    final JsonParser jsonParser;
    final CollaborationControllerImpl collabController;
    final CollabRoleService collabRoleService;
    final AuthControllerImpl authController;
    final RoleControllerImpl roleController;

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
        roleController.create(RoleCreateDto.builder().roleName(DefaultRole.USER.toString()).build());
        authController.createNewUser(UserCreateDto.builder().password("228").username("Alex").build());
        collabRoleService.create(CollabRoleCreateDto.builder().collabRoleName(DefaultCollabRole.CREATOR.toString()).build());
        collabRoleService.create(CollabRoleCreateDto.builder().collabRoleName(DefaultCollabRole.PARTICIPANT.toString()).build());
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
        mockMvc.perform(get("/collabs/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/collabs/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenReturnElement() throws Exception {
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollabResponseDto createdCollaboration = collabController.create(collabCreateDto);
        mockMvc.perform(get("/collabs/id/{id}", createdCollaboration.getCollabId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdCollaboration.getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void create_thenThrowUnauthorizedException() throws Exception {
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
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
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
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
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(put("/collabs/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(put("/collabs/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollabResponseDto collabResponseDto = collabController.create(collabCreateDto);
        CollabCreateDto updatedCollabCreateDto = TestData.getUpdatedCollaborationCreateDto();

        mockMvc.perform(put("/collabs/update/{id}", collabResponseDto.getCollabId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedCollabCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.getByCollabName(updatedCollabCreateDto.getCollabName()).getCollabName(),
                updatedCollabCreateDto.getCollabName());
        Assertions.assertEquals(collabController.getByCollabName(updatedCollabCreateDto.getCollabName()).getCollabId(),
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
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        CollabResponseDto collabResponseDto = collabController.create(collabCreateDto);
        CollabDeleteDto collabDeleteDto = CollabDeleteDto.builder().collabName(collabResponseDto.getCollabName()).build();
        mockMvc.perform(delete("/collabs/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> collabController.getById(UUID.fromString(collabResponseDto.getCollabId())));
    }

    @Test
    void getByCollabName_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/collabs/{collab_name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByCollabName_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/collabs/{collab_name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByCollabName_thenReturnElement() throws Exception {
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.create(collabCreateDto);
        mockMvc.perform(get("/collabs/{collab_name}", collabCreateDto.getCollabName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        CollabResponseDto collabResponseDto = collabController.getByCollabName(collabCreateDto.getCollabName());
        Assertions.assertEquals(collabResponseDto.getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void create_thenAssertCreationDate() throws Exception {
        CollabCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(post("/collabs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(LocalDate.now().toString(),
                collabController.getByCollabName(collabCreateDto.getCollabName()).getCreateTime());
    }

}