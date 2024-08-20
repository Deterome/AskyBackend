package org.senla_project.application.mapper;

import lombok.NonNull;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Autowired
    private UserDao userDao;
    @Autowired
    private QuestionDao questionDao;

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Answer toEntity(@NonNull AnswerDto dto) {
        return Answer.builder()
                .author(userDao.findUserByNickname(dto.getAuthorName()))
                .body(dto.getBody())
                .createTime(LocalDate.parse(dto.getCreateTime(), this.dateTimeFormatter_yyyy_MM_dd))
                .usefulness(dto.getUsefulness())
                .question(questionDao.findById(dto.getQuestionId()))
                .build();
    }

    @Override
    public AnswerDto toDto(@NonNull Answer entity) {
        return AnswerDto.builder()
                .authorName(entity.getAuthor().getNickname())
                .body(entity.getBody())
                .createTime(entity.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .questionId(entity.getQuestion().getId())
                .usefulness(entity.getUsefulness())
            .build();
    }
}
