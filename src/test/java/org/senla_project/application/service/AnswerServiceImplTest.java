package org.senla_project.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.dto.answer.AnswerUpdateDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
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

    void mockSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("Alex");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void create() {
        mockSecurityContext();

        AnswerCreateDto answerCreateDto = TestData.getAnswerCreateDto();
        when(answerMapperMock.toAnswer(answerCreateDto)).thenReturn(TestData.getAnswer());
        answerServiceMock.create(answerCreateDto);
        verify(answerRepositoryMock).save(any());
    }

    @Test
    void update() {
        AnswerUpdateDto answerUpdateDto = TestData.getAnswerUpdateDto();
        UUID id = UUID.randomUUID();
        answerUpdateDto.setAnswerId(id.toString());

        when(answerRepositoryMock.findById(id)).thenReturn(Optional.of(TestData.getAnswer()));

        answerServiceMock.update(answerUpdateDto);

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