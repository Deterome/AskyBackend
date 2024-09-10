package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.mapper.AnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerService implements ServiceInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull AnswerCreateDto element) {
        answerRepository.create(answerMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull AnswerCreateDto updatedElement) {
        answerRepository.update(answerMapper.toEntity(id, updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        answerRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<AnswerResponseDto> getAllElements() {
        return answerMapper.toDtoList(answerRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<AnswerResponseDto> getElementById(@NonNull UUID id) {
        return answerRepository.findById(id)
                .map(answerMapper::toResponseDto);
    }

    @Transactional
    public Optional<UUID> findAnswerId(String authorName, UUID questionId, String body) {
        return answerRepository.findAnswer(authorName, questionId, body).map(Answer::getAnswerId);
    }

}
