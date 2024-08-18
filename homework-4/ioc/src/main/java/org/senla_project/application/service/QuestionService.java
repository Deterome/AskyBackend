package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.QuestionDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dto.QuestionDto;
import org.senla_project.application.db.entities.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class QuestionService implements ServiceInterface<QuestionDto> {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<QuestionDto> getAllElements() {
        return questionDao.findAll().stream().map(QuestionDto::new).toList();
    }

    @Override
    @Nullable
    public QuestionDto getElementById(@NonNull UUID id) {
        var question = questionDao.findById(id);
        if (question == null) return null;
        return new QuestionDto(question);
    }

    @Override
    public void addElement(@NonNull QuestionDto element) {
        Question newElement = Question.builder()
                .header(element.getHeader())
                .body(element.getBody())
                .author(userDao.findUserByNickname(element.getAuthorName()))
                .interesting(element.getInteresting())
                .createTime(element.getCreateTime())
            .build();
        questionDao.create(newElement);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull QuestionDto updatedElement) {
        Question updatedQuestion = Question.builder()
                .header(updatedElement.getHeader())
                .body(updatedElement.getBody())
                .author(userDao.findUserByNickname(updatedElement.getAuthorName()))
                .interesting(updatedElement.getInteresting())
                .createTime(updatedElement.getCreateTime())
            .build();
        updatedQuestion.setId(updatedElement.getQuestionId());
        questionDao.update(id, updatedQuestion);
    }

    @Override
    public void deleteElement(@NonNull QuestionDto element) {
        questionDao.deleteById(element.getQuestionId());
    }

}
