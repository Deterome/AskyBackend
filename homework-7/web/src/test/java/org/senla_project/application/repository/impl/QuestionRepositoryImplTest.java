package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Question;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({DataSourceConfigTest.class, HibernateConfigTest.class, QuestionRepositoryImpl.class})
@Transactional
class QuestionRepositoryImplTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void create() {
        Question expectedQuestion = TestData.getQuestion();
        questionRepository.create(expectedQuestion);
        Question actual = questionRepository.findById(expectedQuestion.getQuestionId()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }

    @Test
    void findById() {
        Question expectedQuestion = TestData.getQuestion();
        questionRepository.create(expectedQuestion);
        Question actual = questionRepository.findById(expectedQuestion.getQuestionId()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }

    @Test
    void findAll() {
        Question question = TestData.getQuestion();
        List<Question> expectedQuestionList = new ArrayList<>();
        expectedQuestionList.add(question);
        questionRepository.create(question);
        List<Question> actualQuestionList = questionRepository.findAll();
        Assertions.assertEquals(expectedQuestionList, actualQuestionList);
    }

    @Test
    void update() {
        Question question = TestData.getQuestion();
        questionRepository.create(question);
        Question expectedQuestion = TestData.getUpdatedQuestion();
        expectedQuestion.setQuestionId(question.getQuestionId());
        questionRepository.update(expectedQuestion);

        Question actual = questionRepository.findById(expectedQuestion.getQuestionId()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }

    @Test
    void deleteById() {
        Question question = TestData.getQuestion();
        questionRepository.create(question);
        var questionId = question.getQuestionId();
        questionRepository.deleteById(questionId);
        Optional<Question> actual = questionRepository.findById(questionId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findQuestion() {
        Question expectedQuestion = TestData.getQuestion();
        questionRepository.create(expectedQuestion);
        Question actual = questionRepository.findQuestion(expectedQuestion.getHeader(),
                expectedQuestion.getBody(),
                expectedQuestion.getAuthor().getNickname()).get();
        Assertions.assertEquals(expectedQuestion, actual);
    }
}