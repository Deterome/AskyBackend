package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.answer.AnswerCreateDto;
import org.senla_project.application.dto.answer.AnswerResponseDto;
import org.senla_project.application.dto.answer.AnswerUpdateDto;
import org.senla_project.application.entity.Answer;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, QuestionMapper.class, UuidMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class AnswerMapper {

    public abstract Answer partialAnswerToAnswer(@MappingTarget Answer targetAnswer, Answer answer);

    @Mappings({
            @Mapping(source = "dto.questionId", target = "question", qualifiedByName = {"QuestionMapper", "toQuestionFromId"}),
            @Mapping(source = "id", target = "answerId")
    })
    public abstract Answer toAnswer(UUID id, AnswerCreateDto dto);

    @Mappings({
            @Mapping(source = "dto.questionId", target = "question", qualifiedByName = {"QuestionMapper", "toQuestionFromId"}),
            @Mapping(target = "answerId", ignore = true)
    })
    public abstract Answer toAnswer(AnswerCreateDto dto);

    public abstract Answer toAnswer(AnswerUpdateDto updateDto);

    @Mappings({
            @Mapping(target = "questionId", expression = "java(entity.getQuestion().getQuestionId())")
    })
    public abstract AnswerCreateDto toAnswerCreateDto(Answer entity);

    @Mappings({
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "authorName", expression = "java(entity.getAuthor().getUsername())"),
            @Mapping(target = "questionId", expression = "java(entity.getQuestion().getQuestionId())")
    })
    public abstract AnswerResponseDto toAnswerResponseDto(Answer entity);

    public abstract List<Answer> toAnswerList(List<AnswerResponseDto> dtoList);

    public abstract List<AnswerResponseDto> toAnswerDtoList(List<Answer> entityList);

}


