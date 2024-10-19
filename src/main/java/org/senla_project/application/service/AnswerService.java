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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AnswerResponseDto create(@NonNull AnswerCreateDto element) {
        return answerMapper.toAnswerResponseDto(answerRepository.save(
                addDependenciesToAnswer(answerMapper.toAnswer(element))
        ));
    }

    @Transactional
    @Override
    public AnswerResponseDto updateById(@NonNull UUID id, @NonNull AnswerCreateDto updatedElement) throws EntityNotFoundException {
        if (!answerRepository.existsById(id)) throw new EntityNotFoundException("Answer not found");
        return answerMapper.toAnswerResponseDto(answerRepository.save(
                addDependenciesToAnswer(answerMapper.toAnswer(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        answerRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AnswerResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = answerRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Answer not found");
        return elements.map(answerMapper::toAnswerResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public AnswerResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return answerRepository.findById(id)
                .map(answerMapper::toAnswerResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    public AnswerResponseDto getByParams(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body) throws EntityNotFoundException {
        return answerRepository.findByAuthorNameAndQuestionIdAndBody(authorName, questionId, body)
                .map(answerMapper::toAnswerResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

    @Transactional(readOnly = true)
    private Answer addDependenciesToAnswer(Answer answer) {
        answer.setAuthor(userMapper.toUser(
                userService.getByUsername(answer.getAuthor().getUsername())
        ));
        answer.setQuestion(questionMapper.toQuestion(
                questionService.getById(
                        answer.getQuestion().getQuestionId()
                )
        ));

        return answer;
    }

}
