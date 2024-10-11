package org.senla_project.application.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.AnswerRepository;
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
        AnswerRepositoryImpl.class,
        QuestionRepositoryImpl.class,
        UserRepositoryImpl.class
})
@Transactional
class AnswerRepositoryImplTest {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        User user = userRepository.create(TestData.getUser());

        Question question = TestData.getQuestion();
        question.setAuthor(user);

        questionRepository.create(question);
    }

    Answer addDependenciesToAnswer(Answer answer) {
        answer.setAuthor(userRepository.findUserByUsername(answer.getAuthor().getUsername()).get());
        answer.setQuestion(questionRepository.findAll().getFirst());
        return answer;
    }

    @Test
    void create() {
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.create(expectedAnswer);
        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void findById() {
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.create(expectedAnswer);
        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void findAll() {
        Answer answer = addDependenciesToAnswer(TestData.getAnswer());
        List<Answer> expectedAnswerList = new ArrayList<>();
        expectedAnswerList.add(answer);
        answerRepository.create(answer);
        List<Answer> actualAnswerList = answerRepository.findAll();
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void update() {
        Answer answer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.create(answer);
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getUpdatedAnswer());
        expectedAnswer.setAnswerId(answer.getAnswerId());
        answerRepository.update(expectedAnswer);

        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void deleteById() {
        Answer answer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.create(answer);
        var answerId = answer.getAnswerId();
        answerRepository.deleteById(answerId);
        Optional<Answer> actual = answerRepository.findById(answerId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findAnswer() {
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.create(expectedAnswer);
        Answer actual = answerRepository.findAnswer(expectedAnswer.getAuthor().getUsername(),
                expectedAnswer.getQuestion().getQuestionId(),
                expectedAnswer.getBody()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }
}