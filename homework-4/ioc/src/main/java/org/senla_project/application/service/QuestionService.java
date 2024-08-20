package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.ProfileListMapper;
import org.senla_project.application.mapper.ProfileMapper;
import org.senla_project.application.mapper.QuestionListMapper;
import org.senla_project.application.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class QuestionService implements ServiceInterface<QuestionDto> {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionListMapper questionListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<QuestionDto> getAllElements() {
        return questionListMapper.toDtoList(questionDao.findAll());
    }

    @Override
    @Nullable
    public QuestionDto getElementById(@NonNull UUID id) {
        Question question = questionDao.findById(id);
        if (question == null) return null;
        return questionMapper.toDto(question);
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
    public void deleteElement(@NonNull QuestionDto element) {
        questionDao.deleteById(element.getQuestionId());
    }

}
