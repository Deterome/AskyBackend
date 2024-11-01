package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionDeleteDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.dto.question.QuestionUpdateDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.service.QuestionService;
import org.senla_project.application.service.linker.QuestionLinkerService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    final private QuestionRepository questionRepository;
    final private QuestionMapper questionMapper;
    final private QuestionLinkerService questionLinkerService;

    @Transactional
    @Override
    public QuestionResponseDto create(@NonNull QuestionCreateDto element) {
        Question question = questionMapper.toQuestion(element);
        questionLinkerService.linkQuestionWithUser(question);
        return questionMapper.toQuestionResponseDto(questionRepository.save(question));
    }

    @Transactional
    @Override
    @PreAuthorize("#updatedQuestion.authorName == authentication.principal.username")
    public QuestionResponseDto update(@NonNull @P("updatedQuestion") QuestionUpdateDto questionUpdateDto) throws EntityNotFoundException {
        if (!questionRepository.existsById(UUID.fromString(questionUpdateDto.getQuestionId()))) throw new EntityNotFoundException("Question join not found");

        Question question = questionMapper.toQuestion(questionUpdateDto);
        questionLinkerService.linkQuestionWithUser(question);
        return questionMapper.toQuestionResponseDto(questionRepository.save(question));
    }

    @Transactional
    @Override
    @PreAuthorize("#deletedQuestion.authorName == authentication.principal.username")
    public void delete(@NonNull @P("deletedQuestion") QuestionDeleteDto questionDeleteDto) {
        questionRepository.deleteById(UUID.fromString(questionDeleteDto.getQuestionId()));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<QuestionResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = questionRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Question not found");
        return elements.map(questionMapper::toQuestionResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return questionRepository.findById(id)
                .map(questionMapper::toQuestionResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionResponseDto getByHeaderAndBodyAndAuthorName(String header, String body, String authorName) throws EntityNotFoundException {
        return questionRepository.findByHeaderAndBodyAndAuthorName(header, body, authorName)
                .map(questionMapper::toQuestionResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

}
