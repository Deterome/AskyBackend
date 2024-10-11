package org.senla_project.application.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.ApplicationConfigTest;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.config.WebSecurityConfig;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
class AnswerControllerTest {

    @Autowired
    JsonParser jsonParser;
    @Autowired
    AnswerController answerController;
    @Autowired
    QuestionController questionController;
    @Autowired
    RoleController roleController;
    @Autowired
    AuthController authController;

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
        authController.createNewUser(TestData.getUserCreateDto());
        questionController.addElement(TestData.getQuestionCreateDto());
    }

    AnswerCreateDto setQuestionIdOfAnswerCreateDto(AnswerCreateDto answerCreateDto) {
        answerCreateDto.setQuestionId(
                UUID.fromString(questionController.getAllElements().getFirst().getQuestionId()
                ));
        return answerCreateDto;
    }

    @Test
    void getAllElements_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAllElements_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAllElements_thenReturnAllElements() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        answerController.addElement(answerCreateDto);
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(answerController.getAllElements().size(), 1);
    }

    @Test
    void findElementById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findElementById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findElementById_thenReturnElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto createdAnswer = answerController.addElement(answerCreateDto);
        mockMvc.perform(get("/answers/{id}", createdAnswer.getAnswerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdAnswer.getBody(),
                answerCreateDto.getBody());
    }

    @Test
    void addElement_thenThrowUnauthorizedException() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(post("/answers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void addElement_thenReturnCreatedElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
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
    void updateElement_thenThrowUnauthorizedException() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(put("/answers/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void updateElement_thenThrowPreconditionFailedException() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(put("/answers/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto)))
                .andDo(print())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void updateElement_thenReturnUpdatedElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto answerResponseDto = answerController.addElement(answerCreateDto);
        AnswerCreateDto updatedAnswerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getUpdatedAnswerCreateDto());
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
    void deleteElement_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/answers/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void deleteElement_thenDeleteElement() throws Exception {
        mockMvc.perform(delete("/answers/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto answerResponseDto = answerController.addElement(answerCreateDto);
        mockMvc.perform(delete("/answers/delete/{id}", answerResponseDto.getAnswerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> answerController.getElementById(UUID.fromString(answerResponseDto.getAnswerId())));
    }

    @Test
    void findAnswerByParams_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findAnswerByParams_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void findAnswerByParams_thenReturnElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
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