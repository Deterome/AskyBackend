package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionService implements ServiceInterface<QuestionDto, QuestionDto> {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull QuestionDto element) {
        questionRepository.create(questionMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull QuestionDto updatedElement) {
        questionRepository.update(id, questionMapper.toEntity(updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        questionRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<QuestionDto> getAllElements() {
        return questionMapper.toDtoList(questionRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<QuestionDto> getElementById(@NonNull UUID id) {
        return questionRepository.findById(id)
                .map(questionMapper::toDto);
    }

    @Transactional
    public Optional<UUID> findQuestionId(String header, String body, String authorName) {
        return questionRepository.findQuestion(header, body, authorName).map(Entity::getId);
    }

}
