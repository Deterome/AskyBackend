package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.AnswerDao;
import org.senla_project.application.db.dao.QuestionDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dto.AnswerDto;
import org.senla_project.application.db.entities.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

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

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<AnswerDto> getAllElements() {
        return answerDao.findAll().stream().map(AnswerDto::new).toList();
    }

    @Override
    @Nullable
    public AnswerDto getElementById(@NonNull UUID id) {
        var answer = answerDao.findById(id);
        if (answer == null) return null;
        return new AnswerDto(answer);
    }

    @Override
    public void addElement(@NonNull AnswerDto element) {
        Answer newElement = Answer.builder()
                .createTime(element.getCreateTime())
                .body(element.getBody())
                .question(questionDao.findById(element.getQuestionId()))
                .usefulness(element.getUsefulness())
                .author(userDao.findUserByNickname(element.getAuthorName()))
            .build();
        answerDao.create(newElement);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull AnswerDto updatedElement) {
        Answer updatedAnswer = Answer.builder()
                .createTime(updatedElement.getCreateTime())
                .body(updatedElement.getBody())
                .question(questionDao.findById(updatedElement.getQuestionId()))
                .usefulness(updatedElement.getUsefulness())
                .author(userDao.findUserByNickname(updatedElement.getAuthorName()))
            .build();
        updatedAnswer.setId(updatedElement.getAnswerId());
        answerDao.update(id, updatedAnswer);
    }

    @Override
    public void deleteElement(@NonNull AnswerDto element) {
        answerDao.deleteById(element.getAnswerId());
    }

}
