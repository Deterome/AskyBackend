package org.senla_project.application.service.linker;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerLinkerService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public Answer linkAnswerWithQuestion(Answer answer) throws EntityLinkerException {
        Question questionInfo = answer.getQuestion();
        answer.setQuestion(questionRepository.findById(
                questionInfo.getQuestionId()
        ).orElseThrow(() -> new EntityLinkerException("Can't link an answer with question. Question not found.")));

        return answer;
    }

    @Transactional(readOnly = true)
    public Answer linkAnswerWithUser(Answer answer) {
        User userInfo = answer.getAuthor();
        answer.setAuthor(userRepository.findByUsername(userInfo.getUsername())
                .orElseThrow(() -> new EntityLinkerException("Can't link an answer with user. User not found."))
        );

        return answer;
    }

}
