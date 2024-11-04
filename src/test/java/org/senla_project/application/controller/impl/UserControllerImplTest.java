package org.senla_project.application.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.*;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.dto.user.UserDeleteDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.dto.user.UserUpdateDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.data.DefaultRole;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
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
class UserControllerImplTest {

    final JsonParser jsonParser;
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
        roleController.create(TestData.getRoleCreateDto());
        roleController.create(RoleCreateDto.builder().roleName(TestData.ADMIN_ROLE).build());
    }

    @Test
    void getAll_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void getAll_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void getAll_thenReturnAll() throws Exception {
        UserResponseDto userResponseDto = authController.createNewUser(TestData.getUserCreateDto());
        mockMvc.perform(get("/users/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(1));
    }

    @Test
    void getById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/users/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/users/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void getById_thenReturnElement() throws Exception {
        UserResponseDto createdUser = authController.createNewUser(TestData.getUserCreateDto());
        mockMvc.perform(get("/users/id/{id}", createdUser.getUserId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(createdUser.getUsername()));
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        UserUpdateDto userUpdateDto = TestData.getUserUpdateDto();
        userUpdateDto.setUserId(UUID.randomUUID().toString());
        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userUpdateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UserResponseDto userResponseDto = authController.createNewUser(userCreateDto);
        UserUpdateDto userUpdateDto = TestData.getUserUpdateDto();
        userUpdateDto.setUserId(userResponseDto.getUserId());

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(userUpdateDto.getUsername()));
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void update_whenUpdatingRoles_thenReturnUpdatedElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        userCreateDto.setUsername("Bob");
        UserResponseDto userResponseDto = authController.createNewUser(userCreateDto);
        UserUpdateDto userUpdateDto = TestData.getUserUpdateDto();
        userUpdateDto.setUsername("Bob");
        userUpdateDto.setRoles(List.of(DefaultRole.USER.toString(), DefaultRole.ADMIN.toString()));
        userUpdateDto.setUserId(userResponseDto.getUserId());

        mockMvc.perform(put("/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("roles", Matchers.hasItems(DefaultRole.ADMIN.toString(), DefaultRole.USER.toString())));
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/users/delete")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void delete_thenDeleteElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UserResponseDto userResponseDto = authController.createNewUser(userCreateDto);
        UserDeleteDto userDeleteDto = UserDeleteDto.builder()
                .username(userResponseDto.getUsername())
                .build();
        mockMvc.perform(delete("/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getByUsername_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/users/{name}", "Bob")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void getByUsername_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/users/{name}", "Bob")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE, TestData.USER_ROLE})
    void getByUsername_thenReturnElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        authController.createNewUser(userCreateDto);
        mockMvc.perform(get("/users/{name}", userCreateDto.getUsername())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(userCreateDto.getUsername()));
    }
}