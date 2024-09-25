package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
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
class QuestionControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    QuestionController questionController;
    @Autowired
    UserController userController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/questions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        questionController.addElement(TestData.getQuestionCreateDto());
        mockMvc.perform(get("/questions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(questionController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/questions?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionController.addElement(questionCreateDto);
        String questionIdString = questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName()).getQuestionId();
        mockMvc.perform(get("/questions?id={id}", questionIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(questionController.findElementById(UUID.fromString(questionIdString)).getQuestionId(),
                questionIdString);
    }

    @Test
    void addElement() throws Exception {
        userController.addElement(TestData.getUserCreateDto());
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(post("/questions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName()).getBody(),
                questionCreateDto.getBody());
    }

    @Test
    void updateElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(put("/questions/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        mockMvc.perform(put("/questions/update?id={id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        questionController.addElement(questionCreateDto);
        QuestionResponseDto questionResponseDto = questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName());
        QuestionCreateDto updatedQuestionCreateDto = TestData.getUpdatedQuestionCreateDto();

        mockMvc.perform(put("/questions/update?id={id}", questionResponseDto.getQuestionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedQuestionCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(questionController.findQuestionByParams(
                        updatedQuestionCreateDto.getHeader(),
                        updatedQuestionCreateDto.getBody(),
                        updatedQuestionCreateDto.getAuthorName())
                    .getBody(),
                updatedQuestionCreateDto.getBody());

        Assertions.assertEquals(questionController.findQuestionByParams(updatedQuestionCreateDto.getHeader(),
                        updatedQuestionCreateDto.getBody(),
                        updatedQuestionCreateDto.getAuthorName())
                    .getQuestionId(),
                questionResponseDto.getQuestionId());
    }

    @Test
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/questions/delete?id={id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        userController.addElement(TestData.getUserCreateDto());
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionController.addElement(questionCreateDto);
        String questionIdString = questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName()).getQuestionId();
        mockMvc.perform(delete("/questions/delete?id={id}", questionIdString)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertThrows(EntityNotFoundException.class, () -> questionController.findElementById(UUID.fromString(questionIdString)));
    }

    @Test
    void findQuestionByParams() throws Exception {
        mockMvc.perform(get("/questions?header={head}&body={body}&author={author}", "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionController.addElement(questionCreateDto);
        mockMvc.perform(get("/questions?header={head}&body={body}&author={author}", questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName()).getBody(),
                questionCreateDto.getBody());
    }

    @Test
    void findQuestion() throws Exception {
        mockMvc.perform(get("/questions?id={id}&header={head}&body={body}&author={author}",  UUID.randomUUID(), "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        userController.addElement(TestData.getUserCreateDto());
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionController.addElement(questionCreateDto);
        String questionIdString = questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName()).getQuestionId();
        mockMvc.perform(get("/questions?id={id}&header={head}&body={body}&author={author}", questionIdString, questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(questionController.findQuestionByParams(questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName()).getBody(),
                questionCreateDto.getBody());

        mockMvc.perform(get("/questions?id={id}&header={head}&body={body}&author={author}", UUID.randomUUID(), questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/questions?id={id}&header={head}&body={body}&author={author}", questionIdString, "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}