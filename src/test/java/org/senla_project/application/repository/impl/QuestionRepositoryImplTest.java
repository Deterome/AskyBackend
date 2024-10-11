package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Question;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        QuestionRepositoryImpl.class,
        UserRepositoryImpl.class
})
@Transactional
class QuestionRepositoryImplTest {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        userRepository.create(TestData.getUser());
    }

    Question addDependenciesToQuestion(Question question) {
        question.setAuthor(userRepository.findUserByUsername(question.getAuthor().getUsername()).get());
        return question;
    }

    @Test
    void create() {
        Question expectedQuestion = addDependenciesToQuestion(TestData.getQuestion());
        questionRepository.create(expectedQuestion);
        Question actual = questionRepository.findById(expectedQuestion.getQuestionId()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }

    @Test
    void findById() {
        Question expectedQuestion = addDependenciesToQuestion(TestData.getQuestion());
        questionRepository.create(expectedQuestion);
        Question actual = questionRepository.findById(expectedQuestion.getQuestionId()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }

    @Test
    void findAll() {
        Question question = addDependenciesToQuestion(TestData.getQuestion());
        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(question);
        questionRepository.create(question);
        List<Question> actualQuestionList = questionRepository.findAll();
        Assertions.assertEquals(expectedQuestionList, actualQuestionList);
    }

    @Test
    void update() {
        Question question = addDependenciesToQuestion(TestData.getQuestion());
        questionRepository.create(question);
        Question expectedQuestion = addDependenciesToQuestion(TestData.getUpdatedQuestion());
        expectedQuestion.setQuestionId(question.getQuestionId());
        questionRepository.update(expectedQuestion);

        Question actual = questionRepository.findById(expectedQuestion.getQuestionId()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }

    @Test
    void deleteById() {
        Question question = addDependenciesToQuestion(TestData.getQuestion());
        questionRepository.create(question);
        var questionId = question.getQuestionId();
        questionRepository.deleteById(questionId);
        Optional<Question> actual = questionRepository.findById(questionId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findQuestion() {
        Question expectedQuestion = addDependenciesToQuestion(TestData.getQuestion());
        questionRepository.create(expectedQuestion);
        Question actual = questionRepository.findQuestion(expectedQuestion.getHeader(),
                expectedQuestion.getBody(),
                expectedQuestion.getAuthor().getUsername()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }
}