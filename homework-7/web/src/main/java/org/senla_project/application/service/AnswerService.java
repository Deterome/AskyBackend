package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
public class AnswerService implements ServiceInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerMapper answerMapper;

    @Transactional
    @Override
    public AnswerResponseDto addElement(@NonNull AnswerCreateDto element) {
        return answerMapper.toResponseDto(answerRepository.create(answerMapper.toEntity(element)));
    }

    @Transactional
    @Override
    public AnswerResponseDto updateElement(@NonNull UUID id, @NonNull AnswerCreateDto updatedElement) {
        return answerMapper.toResponseDto(answerRepository.update(answerMapper.toEntity(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        answerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AnswerResponseDto> getAllElements() throws EntityNotFoundException {
        var elements = answerMapper.toDtoList(answerRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Answers not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public AnswerResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return answerRepository.findById(id)
                .map(answerMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    public AnswerResponseDto findAnswerByParams(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body) throws EntityNotFoundException {
        return answerRepository.findAnswer(authorName, questionId, body)
                .map(answerMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

}
