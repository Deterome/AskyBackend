package org.senla_project.application.service;

import org.hibernate.query.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    AnswerRepository answerRepositoryMock;
    @Mock
    AnswerMapper answerMapperMock;
    @Spy
    UserMapper userMapperSpy;
    @Mock
    UserService userServiceMock;
    @Spy
    QuestionMapper questionMapperSpy;
    @Mock
    QuestionService questionServiceMock;
    @InjectMocks
    AnswerService answerServiceMock;


    @Test
    void create() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        Mockito.when(answerMapperMock.toAnswer(answerCreateDto)).thenReturn(TestData.getAnswer());
        Mockito.when(userMapperSpy.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(questionMapperSpy.toQuestion((QuestionResponseDto) Mockito.any())).thenReturn(TestData.getQuestion());
        answerServiceMock.create(answerCreateDto);
        Mockito.verify(answerRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(answerMapperMock.toAnswer(id, answerCreateDto)).thenReturn(TestData.getAnswer());
        Mockito.when(userMapperSpy.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(questionMapperSpy.toQuestion((QuestionResponseDto) Mockito.any())).thenReturn(TestData.getQuestion());
        Mockito.when(answerRepositoryMock.existsById(id)).thenReturn(true);
        answerServiceMock.updateById(id, answerCreateDto);
        Mockito.verify(answerRepositoryMock).save(Mockito.any());
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(answerRepositoryMock).deleteById(Mockito.any());
        answerServiceMock.deleteById(UUID.randomUUID());
        Mockito.verify(answerRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(answerRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getAnswer()),
                            PageRequest.of(0, 5),
                            1));
            answerServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(answerRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            answerServiceMock.getById(UUID.randomUUID());
            Mockito.verify(answerRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getByParams() {
        try {
            Answer answer = TestData.getAnswer();
            answerServiceMock.getByParams(answer.getAuthor().getUsername(), UUID.randomUUID(), answer.getBody());
            Mockito.verify(answerRepositoryMock).findByAuthorNameAndQuestionIdAndBody(Mockito.any(), Mockito.any(), Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

}