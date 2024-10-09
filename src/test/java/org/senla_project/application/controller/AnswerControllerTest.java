package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class AnswerControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    AnswerController answerController;
    @Autowired
    QuestionController questionController;
    @Autowired
    AuthController authController;

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAllElements() throws Exception {
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        authController.createNewUser(TestData.getUserCreateDto());
        QuestionResponseDto questionResponseDto = questionController.addElement(TestData.getQuestionCreateDto());
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        answerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));
        answerController.addElement(answerCreateDto);
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(answerController.getAllElements().size(), 1);
    }

    @Test
    void findElementById() throws Exception {
        mockMvc.perform(get("/answers/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        authController.createNewUser(TestData.getUserCreateDto());
        QuestionResponseDto questionResponseDto = questionController.addElement(TestData.getQuestionCreateDto());
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        answerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));
        AnswerResponseDto createdAnswer = answerController.addElement(answerCreateDto);
        mockMvc.perform(get("/answers/{id}", createdAnswer.getAnswerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdAnswer.getBody(),
                answerCreateDto.getBody());
    }

    @Test
    void addElement() throws Exception {
        authController.createNewUser(TestData.getUserCreateDto());
        QuestionResponseDto questionResponseDto = questionController.addElement(TestData.getQuestionCreateDto());
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        answerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));
        mockMvc.perform(post("/answers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(answerController.findAnswerByParams(
                        answerCreateDto.getAuthorName(),
                        answerCreateDto.getQuestionId(),
                        answerCreateDto.getBody()
                ).getBody(),
                answerCreateDto.getBody());
    }

    @Test
    void updateElement() throws Exception {
        authController.createNewUser(TestData.getUserCreateDto());
        QuestionResponseDto questionResponseDto = questionController.addElement(TestData.getQuestionCreateDto());
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        answerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));
        mockMvc.perform(put("/answers/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());

        AnswerResponseDto answerResponseDto = answerController.addElement(answerCreateDto);
        AnswerCreateDto updatedAnswerCreateDto = TestData.getUpdatedAnswerCreateDto();
        updatedAnswerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));

        mockMvc.perform(put("/answers/update/{id}", answerResponseDto.getAnswerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedAnswerCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(answerController.findAnswerByParams(
                        updatedAnswerCreateDto.getAuthorName(),
                        updatedAnswerCreateDto.getQuestionId(),
                        updatedAnswerCreateDto.getBody()
                ).getBody(),
                updatedAnswerCreateDto.getBody());
        Assertions.assertEquals(answerController.findAnswerByParams(
                        updatedAnswerCreateDto.getAuthorName(),
                        updatedAnswerCreateDto.getQuestionId(),
                        updatedAnswerCreateDto.getBody()
                ).getAnswerId(),
                answerResponseDto.getAnswerId());
    }

    @Test
    void deleteElement() throws Exception {
        mockMvc.perform(delete("/answers/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        authController.createNewUser(TestData.getUserCreateDto());
        QuestionResponseDto questionResponseDto = questionController.addElement(TestData.getQuestionCreateDto());
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        answerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));
        AnswerResponseDto answerResponseDto = answerController.addElement(answerCreateDto);
        mockMvc.perform(delete("/answers/delete/{id}", answerResponseDto.getAnswerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> answerController.getElementById(UUID.fromString(answerResponseDto.getAnswerId())));
    }

    @Test
    void findAnswerByParams() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        authController.createNewUser(TestData.getUserCreateDto());
        QuestionResponseDto questionResponseDto = questionController.addElement(TestData.getQuestionCreateDto());
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        answerCreateDto.setQuestionId(UUID.fromString(questionResponseDto.getQuestionId()));
        answerController.addElement(answerCreateDto);
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}",
                        answerCreateDto.getAuthorName(),
                        answerCreateDto.getQuestionId(),
                        answerCreateDto.getBody())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        AnswerResponseDto answerResponseDto = answerController.findAnswerByParams(answerCreateDto.getAuthorName(),
                answerCreateDto.getQuestionId(),
                answerCreateDto.getBody());
        Assertions.assertEquals(answerResponseDto.getBody(),
                answerCreateDto.getBody());
    }

}