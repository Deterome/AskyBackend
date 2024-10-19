package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.UserResponseDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    QuestionRepository questionRepositoryMock;
    @Mock
    QuestionMapper questionMapperMock;
    @Spy
    UserMapper userMapper;
    @Mock
    UserService userService;
    @InjectMocks
    QuestionService questionServiceMock;

    @Test
    void create() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        Mockito.when(questionMapperMock.toQuestion(questionCreateDto)).thenReturn(TestData.getQuestion());
        Mockito.when(userMapper.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        questionServiceMock.create(questionCreateDto);
        Mockito.verify(questionRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(questionMapperMock.toQuestion(id, questionCreateDto)).thenReturn(TestData.getQuestion());
        Mockito.when(userMapper.toUser((UserResponseDto) Mockito.any())).thenReturn(TestData.getAuthenticatedUser());
        Mockito.when(questionRepositoryMock.existsById(id)).thenReturn(true);
        questionServiceMock.updateById(id, questionCreateDto);
        Mockito.verify(questionRepositoryMock).save(Mockito.any());
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(questionRepositoryMock).deleteById(Mockito.any());
        questionServiceMock.deleteById(UUID.randomUUID());
        Mockito.verify(questionRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAll() {
        try {
            Mockito.when(questionRepositoryMock.findAll((Pageable) Mockito.any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getQuestion()),
                            PageRequest.of(0, 5),
                            1));
            questionServiceMock.getAll(PageRequest.of(0, 5));
            Mockito.verify(questionRepositoryMock).findAll((Pageable) Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            questionServiceMock.getById(UUID.randomUUID());
            Mockito.verify(questionRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getByParams() {
        try {
            Question question = TestData.getQuestion();
            questionServiceMock.getByParams(question.getHeader(), question.getBody(), question.getAuthor().getUsername());
            Mockito.verify(questionRepositoryMock).findByHeaderAndBodyAndAuthorName(Mockito.any(), Mockito.any(), Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}