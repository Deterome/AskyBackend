package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.UserCreateDto;
import org.senla_project.application.dto.UserResponseDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class UserControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/users/all")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        mockMvc.perform(get("/users/all")
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(userController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/users?id={id}", UUID.randomUUID())
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        userController.addElement(userCreateDto);
        String userIdString = userController.findUserByName(userCreateDto.getNickname()).getUserId();
        mockMvc.perform(get("/users?id={id}", userIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(userController.findElementById(UUID.fromString(userIdString)).getUserId(),
                userIdString);
    }

    @Test
    void addElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        mockMvc.perform(post("/users/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonParser.parseObjectToJson(userCreateDto))
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(userController.findUserByName(userCreateDto.getNickname()).getNickname(),
                userCreateDto.getNickname());
    }

    @Test
    void updateElement() throws Exception {
        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        mockMvc.perform(put("/users/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        userController.addElement(userCreateDto);
        UserResponseDto userResponseDto = userController.findUserByName(userCreateDto.getNickname());
        UserCreateDto updatedUserCreateDto = TestData.getUpdatedUserCreateDto();

        mockMvc.perform(put("/users/update?id={id}", userResponseDto.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedUserCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(userController.findUserByName(updatedUserCreateDto.getNickname()).getNickname(),
                updatedUserCreateDto.getNickname());
        Assertions.assertEquals(userController.findUserByName(updatedUserCreateDto.getNickname()).getUserId(),
                userResponseDto.getUserId());
    }

    @Test
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/users/delete?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        userController.addElement(userCreateDto);
        String userIdString = userController.findUserByName(userCreateDto.getNickname()).getUserId();
        mockMvc.perform(delete("/users/delete?id={id}", userIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.findElementById(UUID.fromString(userIdString)));
    }

    @Test
    void findUserByName() throws Exception {
        mockMvc.perform(get("/users?username={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        userController.addElement(userCreateDto);
        mockMvc.perform(get("/users?username={name}", userCreateDto.getNickname())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(userController.findUserByName(userCreateDto.getNickname()).getNickname(),
                userCreateDto.getNickname());
    }

    @Test
    void findUser() throws Exception {
        mockMvc.perform(get("/users?username={name}&id={id}", "Alex", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        userController.addElement(userCreateDto);
        String userIdString = userController.findUserByName(userCreateDto.getNickname()).getUserId();
        mockMvc.perform(get("/users?username={name}&id={id}", userCreateDto.getNickname(), userIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(userController.findUserByName(userCreateDto.getNickname()).getNickname(),
                userCreateDto.getNickname());

        mockMvc.perform(get("/users?username={name}&id={id}", userCreateDto.getNickname(), UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/users?username={name}&id={id}", "Nick", userIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}