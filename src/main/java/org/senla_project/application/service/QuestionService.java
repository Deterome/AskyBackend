package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
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
    public QuestionResponseDto addElement(@NonNull QuestionCreateDto element) {
        return questionMapper.toQuestionResponseDto(questionRepository.create(
                addDependenciesToQuestion(questionMapper.toQuestion(element))
        ));
    }

    @Transactional
    @Override
    public QuestionResponseDto updateElement(@NonNull UUID id, @NonNull QuestionCreateDto updatedElement) {
        return questionMapper.toQuestionResponseDto(questionRepository.update(
                addDependenciesToQuestion(questionMapper.toQuestion(id, updatedElement))
        ));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        questionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<QuestionResponseDto> findAllElements(int pageNumber) throws EntityNotFoundException {
        var elements = questionMapper.toQuestionDtoList(questionRepository.findAll(pageNumber));
        if (elements.isEmpty()) throw new EntityNotFoundException("Questions not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public QuestionResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return questionRepository.findById(id)
                .map(questionMapper::toQuestionResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    public QuestionResponseDto findQuestionByParams(String header, String body, String authorName) throws EntityNotFoundException {
        return questionRepository.findQuestion(header, body, authorName)
                .map(questionMapper::toQuestionResponseDto).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Transactional(readOnly = true)
    public Question addDependenciesToQuestion(Question question) {
        question.setAuthor(userMapper.toUser(
                userService.findUserByUsername(question.getAuthor().getUsername())
        ));

        return question;
    }

}
