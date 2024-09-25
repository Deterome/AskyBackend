package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.util.TestData;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    AnswerRepository answerRepositoryMock;
    @Spy
    AnswerMapper answerMapperSpy;
    @InjectMocks
    AnswerService answerServiceMock;

    @Test
    void addElement() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        Mockito.doNothing().when(answerRepositoryMock).create(Mockito.any());
        answerServiceMock.addElement(answerCreateDto);
        Mockito.verify(answerRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        Mockito.doNothing().when(answerRepositoryMock).update(Mockito.any());
        answerServiceMock.updateElement(UUID.randomUUID(), answerCreateDto);
        Mockito.verify(answerRepositoryMock).update(Mockito.any());
    }

    @Test
    void deleteElement() {
        Mockito.doNothing().when(answerRepositoryMock).deleteById(Mockito.any());
        answerServiceMock.deleteElement(UUID.randomUUID());
        Mockito.verify(answerRepositoryMock).deleteById(Mockito.any());
    }

    @Test
    void getAllElements() {
        answerServiceMock.getAllElements();
        Mockito.verify(answerRepositoryMock).findAll();
    }

    @Test
    void findElementById() {
        answerServiceMock.findElementById(UUID.randomUUID());
        Mockito.verify(answerRepositoryMock).findById(Mockito.any());
    }

    @Test
    void findAnswer() {
        Answer answer = TestData.getAnswer();
        answerServiceMock.findAnswer(answer.getAuthor().getNickname(), answer.getQuestion().getQuestionId(), answer.getBody());
        Mockito.verify(answerRepositoryMock).findAnswer(Mockito.any(), Mockito.any(), Mockito.any());
    }
}