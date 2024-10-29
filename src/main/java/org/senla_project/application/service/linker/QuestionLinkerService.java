package org.senla_project.application.service.linker;


import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.Question;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionLinkerService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Question linkQuestionWithUser(Question question) {
        User userInfo = question.getAuthor();
        question.setAuthor(userRepository.findByUsername(userInfo.getUsername())
                .orElseThrow(() -> new EntityLinkerException("Can't link a question with user. User not found."))
        );

        return question;
    }

}
