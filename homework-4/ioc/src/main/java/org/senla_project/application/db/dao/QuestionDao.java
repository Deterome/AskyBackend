package org.senla_project.application.db.dao;

import org.senla_project.application.db.entities.Question;

public class QuestionDao extends Dao<Question> {
    public QuestionDao() {
        super(Question.class);
    }
}
