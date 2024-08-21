package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Answer;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, QuestionMapper.class})
public interface AnswerMapper {

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"})
    @Mapping(source = "questionId", target = "question", qualifiedByName = {"QuestionMapper", "toQuestionFromId"})
    Answer toEntity(AnswerDto dto);
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())")
    @Mapping(target = "questionId", expression = "java(entity.getQuestion().getId())")
    AnswerDto toDto(Answer entity);
    List<Answer> toEntityList(List<AnswerDto> dtoList);
    List<AnswerDto> toDtoList(List<Answer> entityList);

}


