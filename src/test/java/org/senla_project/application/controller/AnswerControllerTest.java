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
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.dto.answer.AnswerResponseDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class AnswerControllerTest {

    final JsonParser jsonParser;
    final AnswerController answerController;
    final QuestionController questionController;
    final RoleController roleController;
    final AuthController authController;

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
        authController.createNewUser(TestData.getUserCreateDto());
        questionController.create(TestData.getQuestionCreateDto());
    }

    AnswerCreateDto setQuestionIdOfAnswerCreateDto(AnswerCreateDto answerCreateDto) {
        answerCreateDto.setQuestionId(
                UUID.fromString(questionController.getAll(1, 1).stream().findFirst().get().getQuestionId()
                ));
        return answerCreateDto;
    }

    @Test
    void getAll_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenReturnAllElements() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        answerController.create(answerCreateDto);
        mockMvc.perform(get("/answers/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(answerController.getAll(1, 5).getTotalElements(), 1);
    }

    @Test
    void getById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenReturnElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto createdAnswer = answerController.create(answerCreateDto);
        mockMvc.perform(get("/answers/id/{id}", createdAnswer.getAnswerId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdAnswer.getBody(),
                answerCreateDto.getBody());
    }

    @Test
    void create_thenThrowUnauthorizedException() throws Exception {
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
    void create_thenReturnCreatedElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(post("/answers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(answerController.getByParams(
                        answerCreateDto.getAuthorName(),
                        answerCreateDto.getQuestionId(),
                        answerCreateDto.getBody()
                ).getBody(),
                answerCreateDto.getBody());
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(put("/answers/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(put("/answers/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto answerResponseDto = answerController.create(answerCreateDto);
        AnswerCreateDto updatedAnswerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getUpdatedAnswerCreateDto());
        mockMvc.perform(put("/answers/update/{id}", answerResponseDto.getAnswerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedAnswerCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(answerController.getByParams(
                        updatedAnswerCreateDto.getAuthorName(),
                        updatedAnswerCreateDto.getQuestionId(),
                        updatedAnswerCreateDto.getBody()
                ).getBody(),
                updatedAnswerCreateDto.getBody());
        Assertions.assertEquals(answerController.getByParams(
                        updatedAnswerCreateDto.getAuthorName(),
                        updatedAnswerCreateDto.getQuestionId(),
                        updatedAnswerCreateDto.getBody()
                ).getAnswerId(),
                answerResponseDto.getAnswerId());
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/answers/delete/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void delete_thenDeleteElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto answerResponseDto = answerController.create(answerCreateDto);
        AnswerDeleteDto answerDeleteDto = AnswerDeleteDto.builder()
                .authorName(answerResponseDto.getAuthorName())
                .answerId(answerResponseDto.getAnswerId())
                .build();
        mockMvc.perform(delete("/answers/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertThrows(EntityNotFoundException.class, () -> answerController.getById(UUID.fromString(answerResponseDto.getAnswerId())));
    }

    @Test
    void getByParams_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByParams_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByParams_thenReturnElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        answerController.create(answerCreateDto);
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}",
                        answerCreateDto.getAuthorName(),
                        answerCreateDto.getQuestionId(),
                        answerCreateDto.getBody())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        AnswerResponseDto answerResponseDto = answerController.getByParams(answerCreateDto.getAuthorName(),
                answerCreateDto.getQuestionId(),
                answerCreateDto.getBody());
        Assertions.assertEquals(answerResponseDto.getBody(),
                answerCreateDto.getBody());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void create_thenAssertCreationDate() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        mockMvc.perform(post("/answers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(LocalDate.now().toString(), answerController.getByParams(
                answerCreateDto.getAuthorName(),
                answerCreateDto.getQuestionId(),
                answerCreateDto.getBody()
        ).getCreateTime());
    }

}