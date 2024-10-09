package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AnswerService implements ServiceInterface<UUID, AnswerCreateDto, AnswerResponseDto> {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionMapper questionMapper;

    @Transactional
    @Override
    public AnswerResponseDto addElement(@NonNull AnswerCreateDto element) {
        return answerMapper.toResponseDto(answerRepository.create(
                addDependenciesToAnswer(answerMapper.toEntity(element))
        ));
    }

    @Transactional
    @Override
    public AnswerResponseDto updateElement(@NonNull UUID id, @NonNull AnswerCreateDto updatedElement) {
        return answerMapper.toResponseDto(answerRepository.update(
                addDependenciesToAnswer(answerMapper.toEntity(id, updatedElement))
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
        var elements = answerMapper.toDtoList(answerRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Answers not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public AnswerResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return answerRepository.findById(id)
                .map(answerMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    public AnswerResponseDto findAnswerByParams(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body) throws EntityNotFoundException {
        return answerRepository.findAnswer(authorName, questionId, body)
                .map(answerMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    private Answer addDependenciesToAnswer(Answer answer) {
        answer.setAuthor(userMapper.toUser(
                userService.findUserByUsername(answer.getAuthor().getUsername())
        ));
        answer.setQuestion(questionMapper.toQuestion(
                questionService.findQuestionByParams(
                        answer.getQuestion().getHeader(),
                        answer.getQuestion().getBody(),
                        answer.getQuestion().getAuthor().getUsername()
                    )
        ));

        return answer;
    }

}
