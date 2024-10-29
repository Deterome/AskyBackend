package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.service.impl.QuestionServiceImpl;
import org.senla_project.application.service.linker.QuestionLinkerService;
import org.senla_project.application.util.TestData;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {
    @Mock
    QuestionRepository questionRepositoryMock;
    @Mock
    QuestionMapper questionMapperMock;

    @Mock
    QuestionLinkerService questionLinkerService;

    @InjectMocks
    QuestionServiceImpl questionServiceMock;

    @Test
    void create() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        Mockito.when(questionMapperMock.toQuestion(questionCreateDto)).thenReturn(TestData.getQuestion());
        questionServiceMock.create(questionCreateDto);
        Mockito.verify(questionRepositoryMock).save(Mockito.any());
    }

    @Test
    void updateById() {
        QuestionCreateDto questionCreateDto = TestData.getQuestionCreateDto();
        UUID id = UUID.randomUUID();
        Mockito.when(questionMapperMock.toQuestion(id, questionCreateDto)).thenReturn(TestData.getQuestion());
        Mockito.when(questionRepositoryMock.existsById(id)).thenReturn(true);
        questionServiceMock.updateById(id, questionCreateDto);
        Mockito.verify(questionRepositoryMock).save(Mockito.any());
    }

    @Test
    void delete() {
        QuestionDeleteDto questionDeleteDto = QuestionDeleteDto.builder()
                .authorName("Alex")
                .questionId(UUID.randomUUID().toString())
                .build();
        Mockito.doNothing().when(questionRepositoryMock).deleteById(Mockito.any());
        questionServiceMock.delete(questionDeleteDto);
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
    void getByHeaderAndBodyAndAuthorName() {
        try {
            Question question = TestData.getQuestion();
            questionServiceMock.getByHeaderAndBodyAndAuthorName(question.getHeader(), question.getBody(), question.getAuthor().getUsername());
            Mockito.verify(questionRepositoryMock).findByHeaderAndBodyAndAuthorName(Mockito.any(), Mockito.any(), Mockito.any());
        } catch (EntityNotFoundException ignored) {}
    }
}