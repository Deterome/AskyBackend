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
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.dto.user.UserCreateDto;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
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
class QuestionControllerTest {

    final JsonParser jsonParser;
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
    }

    @Test
    void getAll_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/questions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/questions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getAll_thenReturnAllElements() throws Exception {
        questionController.create(TestData.getQuestionCreateDto());
        mockMvc.perform(get("/questions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(questionController.getAll(1, 5).getTotalElements(), 1);
    }

    @Test
    void getById_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/questions/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/questions/id/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getById_thenReturnElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        QuestionResponseDto createdQuestion = questionController.create(questionCreateDto);
        mockMvc.perform(get("/questions/id/{id}", createdQuestion.getQuestionId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        Assertions.assertEquals(createdQuestion.getBody(),
                questionCreateDto.getBody());
    }

    @Test
    void create_thenThrowUnauthorizedException() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(post("/questions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void create_thenReturnCreatedElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(post("/questions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(questionController.getByParams(questionCreateDto.getHeader(),
                        questionCreateDto.getBody(),
                        questionCreateDto.getAuthorName()).getBody(),
                questionCreateDto.getBody());
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(put("/questions/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(put("/questions/update/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        QuestionResponseDto questionResponseDto = questionController.create(questionCreateDto);
        QuestionCreateDto updatedQuestionCreateDto = TestData.getUpdatedQuestionCreateDto();

        mockMvc.perform(put("/questions/update/{id}", questionResponseDto.getQuestionId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(updatedQuestionCreateDto)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(questionController.getByParams(updatedQuestionCreateDto.getHeader(),
                        updatedQuestionCreateDto.getBody(),
                        updatedQuestionCreateDto.getAuthorName()).getBody(),
                updatedQuestionCreateDto.getBody());
        Assertions.assertEquals(questionController.getByParams(updatedQuestionCreateDto.getHeader(),
                        updatedQuestionCreateDto.getBody(),
                        updatedQuestionCreateDto.getAuthorName()).getQuestionId(),
                questionResponseDto.getQuestionId());
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        QuestionDeleteDto questionDeleteDto = QuestionDeleteDto.builder()
                .questionId(UUID.randomUUID().toString())
                .authorName("Alex")
                .build();
        mockMvc.perform(delete("/questions/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void delete_withInvalidUser_thenThrowForbiddenException() throws Exception {
        authController.createNewUser(UserCreateDto.builder()
                .username("Bob")
                .password("228")
                .build());
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionCreateDto.setAuthorName("Bob");
        QuestionResponseDto questionResponseDto = questionController.create(questionCreateDto);
        QuestionDeleteDto questionDeleteDto = QuestionDeleteDto.builder()
                .questionId(questionResponseDto.getQuestionId())
                .authorName(questionResponseDto.getAuthorName())
                .build();
        mockMvc.perform(delete("/questions/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void delete_thenDeleteElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        QuestionResponseDto questionResponseDto = questionController.create(questionCreateDto);
        QuestionDeleteDto questionDeleteDto = QuestionDeleteDto.builder()
                .questionId(questionResponseDto.getQuestionId())
                .authorName(questionResponseDto.getAuthorName())
                .build();
        mockMvc.perform(delete("/questions/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionDeleteDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getByParams_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/questions?header={header}&body={body}&author={author}", "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByParams_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/questions?header={header}&body={body}&author={author}", "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByParams_thenReturnElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        questionController.create(questionCreateDto);
        mockMvc.perform(get("/questions?header={header}&body={body}&author={author}", questionCreateDto.getHeader(), questionCreateDto.getBody(), questionCreateDto.getAuthorName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        QuestionResponseDto questionResponseDto =
                questionController.getByParams(
                        questionCreateDto.getHeader(),
                        questionCreateDto.getBody(),
                        questionCreateDto.getAuthorName());
        Assertions.assertEquals(questionResponseDto.getBody(),
                questionCreateDto.getBody());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void create_thenAssertCreationDate() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        mockMvc.perform(post("/questions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionCreateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        Assertions.assertEquals(LocalDate.now().toString(),
                questionController.getByParams(questionCreateDto.getHeader(),
                        questionCreateDto.getBody(),
                        questionCreateDto.getAuthorName()).getCreateTime());
    }

}