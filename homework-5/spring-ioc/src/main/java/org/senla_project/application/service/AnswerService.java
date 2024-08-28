package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.daoImpl.AnswerDaoImpl;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerService implements ServiceInterface<AnswerDto, AnswerDto> {

    @Autowired
    private AnswerDaoImpl answerDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public void execute() {}

    @Transaction
    @Override
    public void addElement(@NonNull AnswerDto element) {
        answerDao.create(answerMapper.toEntity(element));
    }

    @Transaction
    @Override
    public void updateElement(@NonNull UUID id, @NonNull AnswerDto updatedElement) {
        answerDao.update(id, answerMapper.toEntity(updatedElement));
    }

    @Transaction
    @Override
    public void deleteElement(@NonNull UUID id) {
        answerDao.deleteById(id);
    }

    @Override
    public List<AnswerDto> getAllElements() {
        return answerMapper.toDtoList(answerDao.findAll());
    }

    @Override
    public Optional<AnswerDto> getElementById(@NonNull UUID id) {
        return answerDao.findById(id)
                .map(answerMapper::toDto);
    }

    public Optional<UUID> findAnswerId(String authorName, UUID questionId, String body) {
        return answerDao.findAnswer(authorName, questionId, body).map(Entity::getId);
    }

}
