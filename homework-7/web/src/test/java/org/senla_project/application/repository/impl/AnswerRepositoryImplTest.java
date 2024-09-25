package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.util.TestData;
import org.senla_project.application.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({DataSourceConfigTest.class, HibernateConfigTest.class, AnswerRepositoryImpl.class})
@Transactional
class AnswerRepositoryImplTest {

    @Autowired
    AnswerRepository answerRepository;

    @Test
    void create() {
        Answer expectedAnswer = TestData.getAnswer();
        answerRepository.create(expectedAnswer);
        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void findById() {
        Answer expectedAnswer = TestData.getAnswer();
        answerRepository.create(expectedAnswer);
        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void findAll() {
        Answer answer = TestData.getAnswer();
        List<Answer> expectedAnswerList = new ArrayList<>();
        expectedAnswerList.add(answer);
        answerRepository.create(answer);
        List<Answer> actualAnswerList = answerRepository.findAll();
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void update() {
        Answer answer = TestData.getAnswer();
        answerRepository.create(answer);
        Answer expectedAnswer = TestData.getUpdatedAnswer();
        expectedAnswer.setAnswerId(answer.getAnswerId());
        answerRepository.update(expectedAnswer);

        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void deleteById() {
        Answer answer = TestData.getAnswer();
        answerRepository.create(answer);
        var answerId = answer.getAnswerId();
        answerRepository.deleteById(answerId);
        Optional<Answer> actual = answerRepository.findById(answerId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findAnswer() {
        Answer expectedAnswer = TestData.getAnswer();
        answerRepository.create(expectedAnswer);
        Answer actual = answerRepository.findAnswer(expectedAnswer.getAuthor().getNickname(),
                expectedAnswer.getQuestion().getQuestionId(),
                expectedAnswer.getBody()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }
}