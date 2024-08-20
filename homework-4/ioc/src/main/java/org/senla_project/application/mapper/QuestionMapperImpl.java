package org.senla_project.application.mapper;

import lombok.NonNull;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Autowired
    private UserDao userDao;

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Question toEntity(@NonNull QuestionDto dto) {
        return Question.builder()
                .header(dto.getHeader())
                .body(dto.getBody())
                .author(userDao.findUserByNickname(dto.getAuthorName()))
                .createTime(LocalDate.parse(dto.getCreateTime(), dateTimeFormatter_yyyy_MM_dd))
                .interesting(dto.getInteresting())
            .build();
    }

    @Override
    public QuestionDto toDto(@NonNull Question entity) {
        return QuestionDto.builder()
                .authorName(entity.getAuthor().getNickname())
                .header(entity.getHeader())
                .body(entity.getBody())
                .interesting(entity.getInteresting())
                .createTime(entity.getCreateTime().format(dateTimeFormatter_yyyy_MM_dd))
            .build();
    }
}
