package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerService implements ServiceInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    final private AnswerRepository answerRepository;
    final private AnswerMapper answerMapper;
    final private UserService userService;
    final private UserMapper userMapper;
    final private QuestionService questionService;
    final private QuestionMapper questionMapper;

    @Transactional
    @Override
    public AnswerResponseDto addElement(@NonNull AnswerCreateDto element) {
        return answerMapper.toAnswerResponseDto(answerRepository.create(
                addDependenciesToAnswer(answerMapper.toAnswer(element))
        ));
    }

    @Transactional
    @Override
    public AnswerResponseDto updateElement(@NonNull UUID id, @NonNull AnswerCreateDto updatedElement) {
        return answerMapper.toAnswerResponseDto(answerRepository.update(
                addDependenciesToAnswer(answerMapper.toAnswer(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        answerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AnswerResponseDto> findAllElements() throws EntityNotFoundException {
        var elements = answerMapper.toAnswerDtoList(answerRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Answers not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public AnswerResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return answerRepository.findById(id)
                .map(answerMapper::toAnswerResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    public AnswerResponseDto findAnswerByParams(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body) throws EntityNotFoundException {
        return answerRepository.findAnswer(authorName, questionId, body)
                .map(answerMapper::toAnswerResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    private Answer addDependenciesToAnswer(Answer answer) {
        answer.setAuthor(userMapper.toUser(
                userService.findUserByUsername(answer.getAuthor().getUsername())
        ));
        answer.setQuestion(questionMapper.toQuestion(
                questionService.findElementById(
                        answer.getQuestion().getQuestionId()
                )
        ));

        return answer;
    }

}
