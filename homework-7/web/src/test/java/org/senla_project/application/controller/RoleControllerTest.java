package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
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
class RoleControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    RoleController roleController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        roleController.addElement(TestData.getRoleCreateDto());
        mockMvc.perform(get("/roles/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(roleController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/roles?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleController.addElement(roleCreateDto);
        String roleIdString = roleController.findRoleByName(roleCreateDto.getRoleName()).getRoleId();
        mockMvc.perform(get("/roles?id={id}", roleIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(roleController.findElementById(UUID.fromString(roleIdString)).getRoleId(),
                roleIdString);
    }

    @Test
    void addElement() throws Exception {
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
    void updateElement() throws Exception {
        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        mockMvc.perform(put("/roles/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(roleCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        roleController.addElement(roleCreateDto);
        RoleResponseDto roleResponseDto = roleController.findRoleByName(roleCreateDto.getRoleName());
        RoleCreateDto updatedRoleCreateDto = TestData.getUpdatedRoleCreateDto();

        mockMvc.perform(put("/roles/update?id={id}", roleResponseDto.getRoleId())
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
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/roles/delete?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleController.addElement(roleCreateDto);
        String roleIdString = roleController.findRoleByName(roleCreateDto.getRoleName()).getRoleId();
        mockMvc.perform(delete("/roles/delete?id={id}", roleIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertThrows(EntityNotFoundException.class, () -> roleController.findElementById(UUID.fromString(roleIdString)));
    }

    @Test
    void findRoleByName() throws Exception {
        mockMvc.perform(get("/roles?role-name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleController.addElement(roleCreateDto);
        mockMvc.perform(get("/roles?role-name={name}", roleCreateDto.getRoleName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(roleController.findRoleByName(roleCreateDto.getRoleName()).getRoleName(),
                roleCreateDto.getRoleName());
    }

    @Test
    void findRole() throws Exception {
        mockMvc.perform(get("/roles?role-name={name}&id={id}", "Alex", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        RoleCreateDto roleCreateDto = TestData.getRoleCreateDto();
        roleController.addElement(roleCreateDto);
        String roleIdString = roleController.findRoleByName(roleCreateDto.getRoleName()).getRoleId();
        mockMvc.perform(get("/roles?role-name={name}&id={id}", roleCreateDto.getRoleName(), roleIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(roleController.findRoleByName(roleCreateDto.getRoleName()).getRoleName(),
                roleCreateDto.getRoleName());

        mockMvc.perform(get("/roles?role-name={name}&id={id}", roleCreateDto.getRoleName(), UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/roles?role-name={name}&id={id}", "Nick", roleIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}