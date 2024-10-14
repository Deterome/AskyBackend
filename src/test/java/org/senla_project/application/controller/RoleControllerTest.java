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
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
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
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class RoleControllerTest {

    final JsonParser jsonParser;
    final RoleController roleController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void getAllElements_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/roles/all?page=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAllElements_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/roles/all?page=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAllElements_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/roles/all?page=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void getAllElements_thenReturnAllElements() throws Exception {
        roleController.addElement(TestData.getRoleCreateDto());
        mockMvc.perform(get("/roles/all?page=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(roleController.getAllElements(1).size(), 1);
    }

    @Test
    void findElementById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/roles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findElementById_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/roles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void findElementById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/roles/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void findElementById_thenReturnElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        RoleResponseDto createdRole = roleController.addElement(roleCreateDto);
        mockMvc.perform(get("/roles/{id}", createdRole.getRoleId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdRole.getRoleName(),
                roleCreateDto.getRoleName());
    }

    @Test
    void addElement_thenThrowUnauthorizedException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void addElement_thenThrowForbiddenException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void addElement_thenReturnCreatedElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(post("/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(roleController.findRoleByName(roleCreateDto.getRoleName()).getRoleName(),
                roleCreateDto.getRoleName());
    }

    @Test
    void updateElement_thenThrowUnauthorizedException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void updateElement_thenThrowForbiddenException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void updateElement_thenThrowPreconditionFailedException() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void updateElement_thenReturnUpdatedElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        RoleResponseDto roleResponseDto = roleController.addElement(roleCreateDto);
        RoleCreateDto updatedRoleCreateDto = TestData.getUpdatedRoleCreateDto();

        mockMvc.perform(put("/roles/update/{id}", roleResponseDto.getRoleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedRoleCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(roleController.findRoleByName(updatedRoleCreateDto.getRoleName()).getRoleName(),
                updatedRoleCreateDto.getRoleName());
        Assertions.assertEquals(roleController.findRoleByName(updatedRoleCreateDto.getRoleName()).getRoleId(),
                roleResponseDto.getRoleId());
    }

    @Test
    void deleteElement_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/roles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void deleteElement_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(delete("/roles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void deleteElement_thenDeleteElement() throws Exception {
        mockMvc.perform(delete("/roles/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        RoleResponseDto roleResponseDto = roleController.addElement(roleCreateDto);
        mockMvc.perform(delete("/roles/delete/{id}", roleResponseDto.getRoleId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleController.getElementById(UUID.fromString(roleResponseDto.getRoleId())));
    }

    @Test
    void findRoleByName_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/roles?role_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findRoleByName_thenThrowForbiddenException() throws Exception {
        mockMvc.perform(get("/roles?role_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void findRoleByName_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/roles?role_name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.ADMIN_ROLE})
    void findRoleByName_thenReturnElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleController.addElement(roleCreateDto);
        mockMvc.perform(get("/roles?role_name={name}", roleCreateDto.getRoleName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        RoleResponseDto roleResponseDto = roleController.findRoleByName(roleCreateDto.getRoleName());
        Assertions.assertEquals(roleResponseDto.getRoleName(),
                roleCreateDto.getRoleName());
    }

}