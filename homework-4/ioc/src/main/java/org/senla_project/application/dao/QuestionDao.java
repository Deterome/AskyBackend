package org.senla_project.application.dao;

import org.senla_project.application.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionDao extends Dao<Question> {
    public QuestionDao() {
        super(Question.class);
    }
}
