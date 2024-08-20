package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.AnswerDao;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.mapper.AnswerListMapper;
import org.senla_project.application.mapper.AnswerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class AnswerService implements ServiceInterface<AnswerDto> {

    @Autowired
    private AnswerDao answerDao;
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private AnswerListMapper answerListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<AnswerDto> getAllElements() {
        return answerListMapper.toDtoList(answerDao.findAll());
    }

    @Override
    @Nullable
    public AnswerDto getElementById(@NonNull UUID id) {
        Answer answer = answerDao.findById(id);
        if (answer == null) return null;
        return answerMapper.toDto(answer);
    }

    @Override
    public void addElement(@NonNull AnswerDto element) {
        answerDao.create(answerMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull AnswerDto updatedElement) {
        answerDao.update(id, answerMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull AnswerDto element) {
        answerDao.deleteById(element.getAnswerId());
    }

}
