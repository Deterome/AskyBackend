package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.TestData;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    QuestionRepository questionRepositoryMock;
    @Spy
    QuestionMapper questionMapperSpy;
    @InjectMocks
    QuestionService questionServiceMock;

    @Test
    void addElement() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        Mockito.doNothing().when(questionRepositoryMock).create(Mockito.any());
        questionServiceMock.addElement(questionCreateDto);
        Mockito.verify(questionRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        Mockito.doNothing().when(questionRepositoryMock).update(Mockito.any());
        questionServiceMock.updateElement(UUID.randomUUID(), questionCreateDto);
        Mockito.verify(questionRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(questionRepositoryMock).deleteById(Mockito.any());
        questionServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(questionRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAllElements() {
        questionServiceMock.getAllElements();
        Mockito.verify(questionRepositoryMock).findAll();
    }

    @Test
    void findElementById() {
        questionServiceMock.findElementById(UUID.randomUUID());
        Mockito.verify(questionRepositoryMock).findById(Mockito.any());
    }

    @Test
    void findQuestion() {
        Question question = TestData.getQuestion();
        questionServiceMock.findQuestion(question.getHeader(), question.getBody(), question.getAuthor().getNickname());
        Mockito.verify(questionRepositoryMock).findQuestion(Mockito.any(), Mockito.any(), Mockito.any());
    }
}