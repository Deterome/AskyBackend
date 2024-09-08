package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.senla_project.application.dto.AnswerCreateDto;
import org.senla_project.application.dto.AnswerResponseDto;
import org.senla_project.application.entity.Answer;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, QuestionMapper.class, UuidMapper.class})
public interface AnswerMapper {

    @Mappings({
        @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "dto.authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"}),
        @Mapping(source = "dto.questionId", target = "question", qualifiedByName = {"QuestionMapper", "toQuestionFromId"})
    })
    Answer toEntity(UUID id, AnswerCreateDto dto);
    default Answer toEntity(AnswerCreateDto dto) {
        return toEntity(UUID.randomUUID(), dto);
    }
    @Mappings({
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())"),
            @Mapping(target = "questionId", expression = "java(entity.getQuestion().getQuestionId())")
    })
    AnswerCreateDto toCreateDto(Answer entity);
    @Mappings({
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())"),
            @Mapping(target = "questionId", expression = "java(entity.getQuestion().getQuestionId())")
    })
    AnswerResponseDto toResponseDto(Answer entity);
    List<Answer> toEntityList(List<AnswerResponseDto> dtoList);
    List<AnswerResponseDto> toDtoList(List<Answer> entityList);

}


