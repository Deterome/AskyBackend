package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.repository.daoImpl.AnswerDaoImpl;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.AnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerService implements ServiceInterface<AnswerDto, AnswerDto> {

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
    public void addElement(@NonNull AnswerDto element) {
        answerRepository.create(answerMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull AnswerDto updatedElement) {
        answerRepository.update(id, answerMapper.toEntity(updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        answerRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<AnswerDto> getAllElements() {
        return answerMapper.toDtoList(answerRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<AnswerDto> getElementById(@NonNull UUID id) {
        return answerRepository.findById(id)
                .map(answerMapper::toDto);
    }

    @Transactional
    public Optional<UUID> findAnswerId(String authorName, UUID questionId, String body) {
        return answerRepository.findAnswer(authorName, questionId, body).map(Entity::getId);
    }

}
