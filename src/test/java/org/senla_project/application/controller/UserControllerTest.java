package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.config.WebSecurityConfig;
import org.senla_project.application.dto.*;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class UserControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    UserController userController;
    @Autowired
    AuthController authController;
    @Autowired
    RoleController roleController;

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
    }

    @Test
    void getAllElements_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAllElements_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAllElements_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAllElements_thenReturnAllElements() throws Exception {
        authController.createNewUser(TestData.getUserCreateDto());
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(userController.getAllElements().size(), 1);
    }

    @Test
    void getElementById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/users/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getElementById_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/users/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getElementById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/users/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getElementById_thenReturnElement() throws Exception {
        UserResponseDto createdUser = authController.createNewUser(TestData.getUserCreateDto());
        mockMvc.perform(get("/users/{id}", createdUser.getUserId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        UserResponseDto userResponseDto = userController.findUserByName(createdUser.getUsername());
        Assertions.assertEquals(userResponseDto.getUsername(),
                createdUser.getUsername());
    }

    @Test
    void updateElement_thenReturnUnauthorizedException() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        mockMvc.perform(put("/users/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void updateElement_thenReturnForbiddenException() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        mockMvc.perform(put("/users/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userCreateDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void updateElement_thenThrowPreconditionFailedException() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        mockMvc.perform(put("/users/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void updateElement_thenReturnUpdatedElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UserResponseDto userResponseDto = authController.createNewUser(userCreateDto);
        UserCreateDto updatedUserCreateDto = TestData.getUpdatedUserCreateDto();

        mockMvc.perform(put("/users/update/{id}", userResponseDto.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedUserCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(userController.findUserByName(updatedUserCreateDto.getUsername()).getUsername(),
                updatedUserCreateDto.getUsername());
        Assertions.assertEquals(userController.findUserByName(updatedUserCreateDto.getUsername()).getUserId(),
                userResponseDto.getUserId());
    }

    @Test
    void deleteElement_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    } 
    
    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void deleteElement_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void deleteElement_thenDeleteElement() throws Exception {
        mockMvc.perform(delete("/users/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UserResponseDto userResponseDto = authController.createNewUser(userCreateDto);
        mockMvc.perform(delete("/users/delete/{id}", userResponseDto.getUserId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.getElementById(UUID.fromString(userResponseDto.getUserId())));
    }

    @Test
    void findUserByName_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/users?username={name}", "Bob")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findUserByName_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/users?username={name}", "Bob")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void findUserByName_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/users?username={name}", "Bob")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void findUserByName_thenReturnElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        authController.createNewUser(userCreateDto);
        mockMvc.perform(get("/users?username={name}", userCreateDto.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        UserResponseDto userResponseDto = userController.findUserByName(userCreateDto.getUsername());
        Assertions.assertEquals(userResponseDto.getUsername(),
                userCreateDto.getUsername());
    }
}