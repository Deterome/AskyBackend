package org.senla_project.application.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.*;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.dto.question.QuestionUpdateDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, WebConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class QuestionControllerImplTest {

    final JsonParser jsonParser;
    final QuestionControllerImpl questionController;
    final RoleControllerImpl roleController;
    final AuthControllerImpl authController;

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(1));
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("body").value(questionCreateDto.getBody()));
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(put("/questions/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        QuestionUpdateDto questionUpdateDto = TestData.getQuestionUpdateDto();
        questionUpdateDto.setQuestionId(UUID.randomUUID().toString());
        mockMvc.perform(put("/questions/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionUpdateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        QuestionResponseDto questionResponseDto = questionController.create(questionCreateDto);
        QuestionUpdateDto questionUpdateDto = TestData.getQuestionUpdateDto();
        questionUpdateDto.setQuestionId(questionResponseDto.getQuestionId());

        mockMvc.perform(put("/questions/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(questionUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").value(questionUpdateDto.getBody()))
                .andExpect(jsonPath("questionId").value(questionResponseDto.getQuestionId()));
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
    void getByHeaderAndBodyAndAuthorName_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/questions?header={header}&body={body}&author={author}", "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByHeaderAndBodyAndAuthorName_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/questions?header={header}&body={body}&author={author}", "123", "123", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByHeaderAndBodyAndAuthorName_thenReturnElement() throws Exception {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        QuestionResponseDto questionResponseDto = questionController.create(questionCreateDto);
        mockMvc.perform(get("/questions?header={header}&body={body}&author={author}",
                        questionResponseDto.getHeader(),
                        questionResponseDto.getBody(),
                        questionResponseDto.getAuthorName())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").value(questionCreateDto.getBody()));
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("createTime").value(LocalDate.now().toString()));
    }

}