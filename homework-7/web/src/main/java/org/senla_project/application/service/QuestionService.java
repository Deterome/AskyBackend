package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public void addElement(@NonNull QuestionCreateDto element) {
        questionRepository.create(questionMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull QuestionCreateDto updatedElement) {
        questionRepository.update(questionMapper.toEntity(id, updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        questionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<QuestionResponseDto> getAllElements() {
        return questionMapper.toDtoList(questionRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<QuestionResponseDto> findElementById(@NonNull UUID id) {
        return questionRepository.findById(id)
                .map(questionMapper::toResponseDto);
    }

    @Transactional
    public Optional<QuestionResponseDto> findQuestion(String header, String body, String authorName) {
        return questionRepository.findQuestion(header, body, authorName)
                .map(questionMapper::toResponseDto);
    }

}
