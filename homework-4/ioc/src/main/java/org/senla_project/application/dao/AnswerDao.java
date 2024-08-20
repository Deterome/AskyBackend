package org.senla_project.application.dao;

import org.senla_project.application.entity.Answer;
import org.springframework.stereotype.Component;

@Component
public class AnswerDao extends Dao<Answer> {
    public AnswerDao() {
        super(Answer.class);
    }
}