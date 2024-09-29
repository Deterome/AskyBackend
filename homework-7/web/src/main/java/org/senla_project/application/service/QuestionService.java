package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService implements ServiceInterface<UUID, QuestionCreateDto, QuestionResponseDto> {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionMapper questionMapper;

    @Transactional
    @Override
    public QuestionResponseDto addElement(@NonNull QuestionCreateDto element) {
        return questionMapper.toResponseDto(questionRepository.create(questionMapper.toEntity(element)));
    }

    @Transactional
    @Override
    public QuestionResponseDto updateElement(@NonNull UUID id, @NonNull QuestionCreateDto updatedElement) {
        return questionMapper.toResponseDto(questionRepository.update(questionMapper.toEntity(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        questionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<QuestionResponseDto> getAllElements() throws EntityNotFoundException {
        var elements = questionMapper.toDtoList(questionRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Questions not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return questionRepository.findById(id)
                .map(questionMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    public QuestionResponseDto findQuestionByParams(String header, String body, String authorName) throws EntityNotFoundException {
        return questionRepository.findQuestion(header, body, authorName)
                .map(questionMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

}
