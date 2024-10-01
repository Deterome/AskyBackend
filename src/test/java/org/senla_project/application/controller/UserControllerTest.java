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
        mockMvc.perform(get("/users/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UserResponseDto createdUser = userController.addElement(userCreateDto);
        mockMvc.perform(get("/users/{id}", createdUser.getUserId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdUser.getNickname(),
                userCreateDto.getNickname());
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
        mockMvc.perform(put("/users/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(userCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        UserResponseDto userResponseDto = userController.addElement(userCreateDto);
        UserCreateDto updatedUserCreateDto = TestData.getUpdatedUserCreateDto();

        mockMvc.perform(put("/users/update/{id}", userResponseDto.getUserId())
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
        mockMvc.perform(delete("/users/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        UserCreateDto userCreateDto = TestData.getUserCreateDto();
        UserResponseDto userResponseDto = userController.addElement(userCreateDto);
        mockMvc.perform(delete("/users/delete/{id}", userResponseDto.getUserId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> userController.getElementById(UUID.fromString(userResponseDto.getUserId())));
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
        UserResponseDto userResponseDto = userController.findUserByName(userCreateDto.getNickname());
        Assertions.assertEquals(userResponseDto.getNickname(),
                userCreateDto.getNickname());
    }

}