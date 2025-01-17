package org.senla_project.application.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.senla_project.application.config.DataSourceConfigTest;
import org.senla_project.application.config.HibernateConfigTest;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.User;
import org.senla_project.application.util.SpringParameterResolver;
import org.senla_project.application.util.TestData;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@SpringJUnitWebConfig({
        DataSourceConfigTest.class,
        HibernateConfigTest.class,
        AnswerRepository.class,
        QuestionRepository.class,
        UserRepository.class
})
@Transactional
@ExtendWith(SpringParameterResolver.class)
@RequiredArgsConstructor
class AnswerRepositoryTest {

    final AnswerRepository answerRepository;
    final QuestionRepository questionRepository;
    final UserRepository userRepository;

    @BeforeEach
    void initDataBaseWithData() {
        User user = userRepository.save(TestData.getUser());

        Question question = TestData.getQuestion();
        question.setAuthor(user);

        questionRepository.save(question);
    }

    Answer addDependenciesToAnswer(Answer answer) {
        answer.setAuthor(userRepository.findByUsername(answer.getAuthor().getUsername()).get());
        answer.setQuestion(questionRepository.findAll().getFirst());
        return answer;
    }

    @Test
    void create() {
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.save(expectedAnswer);
        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void findById() {
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.save(expectedAnswer);
        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void findAll() {
        Answer answer = addDependenciesToAnswer(TestData.getAnswer());
        List<Answer> expectedAnswerList = new ArrayList<>();
        expectedAnswerList.add(answer);
        answerRepository.save(answer);
        List<Answer> actualAnswerList = answerRepository.findAll();
        Assertions.assertEquals(expectedAnswerList, actualAnswerList);
    }

    @Test
    void update() {
        Answer answer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.save(answer);
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getUpdatedAnswer());
        expectedAnswer.setAnswerId(answer.getAnswerId());
        answerRepository.save(expectedAnswer);

        Answer actual = answerRepository.findById(expectedAnswer.getAnswerId()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }

    @Test
    void deleteById() {
        Answer answer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.save(answer);
        var answerId = answer.getAnswerId();
        answerRepository.deleteById(answerId);
        Optional<Answer> actual = answerRepository.findById(answerId);
        Assertions.assertFalse(actual.isPresent());
    }

    @Test
    void findByAuthorNameAndQuestionIdAndBody() {
        Answer expectedAnswer = addDependenciesToAnswer(TestData.getAnswer());
        answerRepository.save(expectedAnswer);
        Answer actual = answerRepository.findByAuthorNameAndQuestionIdAndBody(expectedAnswer.getAuthor().getUsername(),
                expectedAnswer.getQuestion().getQuestionId(),
                expectedAnswer.getBody()).get();
        Assertions.assertEquals(expectedAnswer, actual);
    }
}