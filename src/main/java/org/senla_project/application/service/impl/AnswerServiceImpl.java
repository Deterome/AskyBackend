package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerDeleteDto;
import org.senla_project.application.dto.answer.AnswerResponseDto;
import org.senla_project.application.dto.answer.AnswerUpdateDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.User;
import org.senla_project.application.mapper.AnswerMapper;
import org.senla_project.application.repository.AnswerRepository;
import org.senla_project.application.service.AnswerService;
import org.senla_project.application.service.linker.AnswerLinkerService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.ForbiddenException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    final private AnswerRepository answerRepository;
    final private AnswerMapper answerMapper;
    final private AnswerLinkerService answerLinkerService;

    @Transactional
    @Override
    public AnswerResponseDto create(@NonNull AnswerCreateDto element) {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Answer answer = answerMapper.toAnswer(element);
        answer.setAuthor(User.builder().username(authenticatedUser.getUsername()).build());
        answerLinkerService.linkAnswerWithUser(answer);
        answerLinkerService.linkAnswerWithQuestion(answer);

        return answerMapper.toAnswerResponseDto(answerRepository.save(answer));
    }

    @Transactional
    @Override
    public AnswerResponseDto update(@NonNull AnswerUpdateDto answerUpdateDto) throws EntityNotFoundException {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Answer> oldAnswer = answerRepository.findById(UUID.fromString(answerUpdateDto.getAnswerId()));
        if (oldAnswer.isEmpty()) throw new EntityNotFoundException("Answer not found");
        if (oldAnswer.get().getAuthor().getUsername().equals(authenticatedUser.getUsername())) {
            Answer answer = answerMapper.toAnswer(answerUpdateDto);
            answer.setAuthor(oldAnswer.get().getAuthor());
            answer.setQuestion(oldAnswer.get().getQuestion());

            return answerMapper.toAnswerResponseDto(answerRepository.save(answer));
        } else {
            throw new ForbiddenException(String.format("Answer of user %s is not yours! You can't update this answer!", oldAnswer.get().getAuthor().getUsername()));
        }
    }

    @Transactional
    @Override
    public void delete(@NonNull AnswerDeleteDto answerDeleteDto) {
        answerRepository.deleteById(UUID.fromString(answerDeleteDto.getAnswerId()));
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
    @Override
    public AnswerResponseDto getByAuthorNameQuestionIdAndBody(@NonNull String authorName, @NonNull UUID questionId, @NonNull String body) throws EntityNotFoundException {
        return answerRepository.findByAuthorNameAndQuestionIdAndBody(authorName, questionId, body)
                .map(answerMapper::toAnswerResponseDto).orElseThrow(() -> new EntityNotFoundException("Answer not found"));
    }

}
