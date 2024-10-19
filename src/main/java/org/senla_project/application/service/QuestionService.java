package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.ProfileResponseDto;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionService implements ServiceInterface<UUID, QuestionCreateDto, QuestionResponseDto> {

    final private QuestionRepository questionRepository;
    final private QuestionMapper questionMapper;
    final private UserService userService;
    final private UserMapper userMapper;

    @Transactional
    @Override
    public QuestionResponseDto create(@NonNull QuestionCreateDto element) {
        return questionMapper.toQuestionResponseDto(questionRepository.save(
                addDependenciesToQuestion(questionMapper.toQuestion(element))
        ));
    }

    @Transactional
    @Override
    public QuestionResponseDto updateById(@NonNull UUID id, @NonNull QuestionCreateDto updatedElement) throws EntityNotFoundException {
        if (!questionRepository.existsById(id)) throw new EntityNotFoundException("Question join not found");
        return questionMapper.toQuestionResponseDto(questionRepository.save(
                addDependenciesToQuestion(questionMapper.toQuestion(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        questionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<QuestionResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = questionRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Question not found");
        return elements.map(questionMapper::toQuestionResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return questionRepository.findById(id)
                .map(questionMapper::toQuestionResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    public QuestionResponseDto getByParams(String header, String body, String authorName) throws EntityNotFoundException {
        return questionRepository.findByHeaderAndBodyAndAuthorName(header, body, authorName)
                .map(questionMapper::toQuestionResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    public Question addDependenciesToQuestion(Question question) {
        question.setAuthor(userMapper.toUser(
                userService.getByUsername(question.getAuthor().getUsername())
        ));

        return question;
    }

}
