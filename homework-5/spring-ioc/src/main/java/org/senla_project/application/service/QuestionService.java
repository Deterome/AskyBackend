package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionService implements ServiceInterface<QuestionDto, QuestionDto> {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public void execute() {}

    @Override
    public List<QuestionDto> getAllElements() {
        return questionMapper.toDtoList(questionDao.findAll());
    }

    @Override
    public Optional<QuestionDto> getElementById(@NonNull UUID id) {
        return questionDao.findById(id)
                .map(questionMapper::toDto);
    }

    @Override
    public void addElement(@NonNull QuestionDto element) {
        questionDao.create(questionMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull QuestionDto updatedElement) {
        questionDao.update(id, questionMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        questionDao.deleteById(id);
    }

    public Optional<UUID> findQuestionId(String header, String body, String authorName) {
        return questionDao.findQuestion(header, body, authorName).map(Entity::getId);
    }

}
