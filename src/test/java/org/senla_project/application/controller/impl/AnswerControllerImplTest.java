package org.senla_project.application.controller.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.*;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.dto.answer.AnswerResponseDto;
import org.senla_project.application.dto.answer.AnswerUpdateDto;
import org.senla_project.application.dto.user.UserResponseDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.JsonParser;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.sort.QuestionSortType;
import org.senla_project.application.util.sort.SortOrder;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringJUnitWebConfig(classes = {ApplicationConfigTest.class, WebSecurityConfig.class, WebConfigTest.class, DataSourceConfigTest.class, HibernateConfigTest.class})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class AnswerControllerImplTest {

    final JsonParser jsonParser;
    final AnswerControllerImpl answerController;
    final QuestionControllerImpl questionController;
    final QuestionRepository questionRepository;
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
        UserResponseDto userResponseDto = authController.createNewUser(TestData.getUserCreateDto());
//        questionController.create(TestData.getQuestionCreateDto());
        Question question = TestData.getQuestion();
        question.setAuthor(User.builder()
                .userId(UUID.fromString(userResponseDto.getUserId()))
                .build());
        questionRepository.save(question);
    }

    AnswerCreateDto setQuestionIdOfAnswerCreateDto(AnswerCreateDto answerCreateDto) {
        answerCreateDto.setQuestionId(
                UUID.fromString(questionController.getAll(1, 1, QuestionSortType.CREATE_TIME, SortOrder.ASCENDING).stream().findFirst().get().getQuestionId()
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalElements").value(1));
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("body").value(answerCreateDto.getBody()));
    }

    @Test
    void update_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(put("/answers/update")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenThrowNotFoundException() throws Exception {
        AnswerUpdateDto answerUpdateDto = TestData.getAnswerUpdateDto();
        answerUpdateDto.setAnswerId(UUID.randomUUID().toString());
        mockMvc.perform(put("/answers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerUpdateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void update_thenReturnUpdatedElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto answerResponseDto = answerController.create(answerCreateDto);
        AnswerUpdateDto answerUpdateDto = TestData.getAnswerUpdateDto();
        answerUpdateDto.setAnswerId(answerResponseDto.getAnswerId());
        mockMvc.perform(put("/answers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParser.parseObjectToJson(answerUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").value(answerUpdateDto.getBody()))
                .andExpect(jsonPath("answerId").value(answerResponseDto.getAnswerId()));
    }

    @Test
    void delete_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(delete("/answers/delete")
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
    @WithAnonymousUser
    void getByAuthorNameQuestionIdAndBody_thenThrowUnauthorizedException() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByAuthorNameQuestionIdAndBody_thenThrowNotFoundException() throws Exception {
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}", "123", UUID.randomUUID(), "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = TestData.AUTHORIZED_USER_NAME, authorities = {TestData.USER_ROLE})
    void getByAuthorNameQuestionIdAndBody_thenReturnElement() throws Exception {
        AnswerCreateDto answerCreateDto = setQuestionIdOfAnswerCreateDto(TestData.getAnswerCreateDto());
        AnswerResponseDto answerResponseDto = answerController.create(answerCreateDto);
        mockMvc.perform(get("/answers?author={author}&question_id={question_id}&body={body}",
                        answerResponseDto.getAuthorName(),
                        answerResponseDto.getQuestionId(),
                        answerResponseDto.getBody())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("body").value(answerResponseDto.getBody()));
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("createTime").value(LocalDate.now().toString()));
    }

}