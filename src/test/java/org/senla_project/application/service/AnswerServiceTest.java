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
import org.senla_project.application.util.exception.EntityNotFoundException;

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
        answerServiceMock.addElement(answerCreateDto);
        Mockito.verify(answerRepositoryMock).create(Mockito.any());
    }

    @Test
    void updateElement() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
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
    void findAllElements() {
        try {
            answerServiceMock.findAllElements();
            Mockito.verify(answerRepositoryMock).findAll();
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findElementById() {
        try {
            answerServiceMock.findElementById(UUID.randomUUID());
            Mockito.verify(answerRepositoryMock).findById(Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void findAnswerByParams() {
        try {
            Answer answer = TestData.getAnswer();
            answerServiceMock.findAnswerByParams(answer.getAuthor().getUsername(), UUID.randomUUID(), answer.getBody());
            Mockito.verify(answerRepositoryMock).findAnswer(Mockito.any(), Mockito.any(), Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }

}