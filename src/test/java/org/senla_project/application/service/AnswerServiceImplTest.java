package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.service.impl.AnswerServiceImpl;
import org.senla_project.application.service.linker.AnswerLinkerService;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    AnswerRepository answerRepositoryMock;
    @Mock
    AnswerMapper answerMapperMock;
    @Mock
    AnswerLinkerService answerLinkerService;
    @InjectMocks
    AnswerServiceImpl answerServiceMock;


    @Test
    void create() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        when(answerMapperMock.toAnswer(answerCreateDto)).thenReturn(TestData.getAnswer());
        answerServiceMock.create(answerCreateDto);
        verify(answerRepositoryMock).save(any());
    }

    @Test
    void updateById() {
        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        UUID id = UUID.randomUUID();
        when(answerMapperMock.toAnswer(id, answerCreateDto)).thenReturn(TestData.getAnswer());
        when(answerRepositoryMock.existsById(id)).thenReturn(true);
        answerServiceMock.updateById(id, answerCreateDto);
        verify(answerRepositoryMock).save(any());
    }

    @Test
    void delete() {
        AnswerDeleteDto answerDeleteDto = AnswerDeleteDto.builder()
                .answerId(UUID.randomUUID().toString())
                .authorName("Alex")
                .build();
        doNothing().when(answerRepositoryMock).deleteById(any());
        answerServiceMock.delete(answerDeleteDto);
        verify(answerRepositoryMock).deleteById(any());
    }

    @Test
    void getAll() {
        try {
            when(answerRepositoryMock.findAll((Pageable) any()))
                    .thenReturn(new PageImpl<>(
                            List.of(TestData.getAnswer()),
                            PageRequest.of(0, 5),
                            1));
            answerServiceMock.getAll(PageRequest.of(0, 5));
            verify(answerRepositoryMock).findAll((Pageable) any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getById() {
        try {
            answerServiceMock.getById(UUID.randomUUID());
            verify(answerRepositoryMock).findById(any());
        } catch (EntityNotFoundException ignored) {}
    }

    @Test
    void getByAuthorNameQuestionIdAndBody() {
        try {
            Answer answer = TestData.getAnswer();
            answerServiceMock.getByAuthorNameQuestionIdAndBody(answer.getAuthor().getUsername(), UUID.randomUUID(), answer.getBody());
            verify(answerRepositoryMock).findByAuthorNameAndQuestionIdAndBody(any(), any(), any());
        } catch (EntityNotFoundException ignored) {}
    }

}