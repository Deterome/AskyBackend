package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class CollaborationControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    CollaborationController collabController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        collabController.addElement(TestData.getCollaborationCreateDto());
        mockMvc.perform(get("/collabs/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/collabs?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.addElement(collabCreateDto);
        String collabIdString = collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabId();
        mockMvc.perform(get("/collabs?id={id}", collabIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(collabController.findElementById(UUID.fromString(collabIdString)).getCollabId(),
                collabIdString);
    }

    @Test
    void addElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(post("/collabs/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void updateElement() throws Exception {
        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        mockMvc.perform(put("/collabs/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(collabCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        collabController.addElement(collabCreateDto);
        CollaborationResponseDto collabResponseDto = collabController.findCollabByName(collabCreateDto.getCollabName());
        CollaborationCreateDto updatedCollaborationCreateDto = TestData.getUpdatedCollaborationCreateDto();

        mockMvc.perform(put("/collabs/update?id={id}", collabResponseDto.getCollabId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedCollaborationCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(collabController.findCollabByName(updatedCollaborationCreateDto.getCollabName()).getCollabName(),
                updatedCollaborationCreateDto.getCollabName());
        Assertions.assertEquals(collabController.findCollabByName(updatedCollaborationCreateDto.getCollabName()).getCollabId(),
                collabResponseDto.getCollabId());
    }

    @Test
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/collabs/delete?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.addElement(collabCreateDto);
        String collabIdString = collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabId();
        mockMvc.perform(delete("/collabs/delete?id={id}", collabIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertThrows(EntityNotFoundException.class, () -> collabController.findElementById(UUID.fromString(collabIdString)));
    }

    @Test
    void findCollabByName() throws Exception {
        mockMvc.perform(get("/collabs?collab-name={name}", "Alex")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.addElement(collabCreateDto);
        mockMvc.perform(get("/collabs?collab-name={name}", collabCreateDto.getCollabName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabName(),
                collabCreateDto.getCollabName());
    }

    @Test
    void findCollaboration() throws Exception {
        mockMvc.perform(get("/collabs?collab-name={name}&id={id}", "Alex", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        CollaborationCreateDto collabCreateDto = TestData.getCollaborationCreateDto();
        collabController.addElement(collabCreateDto);
        String collabIdString = collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabId();
        mockMvc.perform(get("/collabs?collab-name={name}&id={id}", collabCreateDto.getCollabName(), collabIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(collabController.findCollabByName(collabCreateDto.getCollabName()).getCollabName(),
                collabCreateDto.getCollabName());

        mockMvc.perform(get("/collabs?collab-name={name}&id={id}", collabCreateDto.getCollabName(), UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/collabs?collab-name={name}&id={id}", "Nick", collabIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}