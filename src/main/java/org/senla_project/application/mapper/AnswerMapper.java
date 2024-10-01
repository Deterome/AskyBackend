package org.senla_project.application.mapper;

import lombok.AllArgsConstructor;
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
public abstract class AnswerMapper {

    @Mappings({
        @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "dto.authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"}),
        @Mapping(source = "dto.questionId", target = "question", qualifiedByName = {"QuestionMapper", "toQuestionFromId"}),
        @Mapping(source = "id", target = "answerId")
    })
    public abstract Answer toEntity(UUID id, AnswerCreateDto dto);
    public Answer toEntity(AnswerCreateDto dto) {
        return toEntity(null, dto);
    }
    @Mappings({
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())"),
            @Mapping(target = "questionId", expression = "java(entity.getQuestion().getQuestionId())")
    })
    public abstract AnswerCreateDto toCreateDto(Answer entity);
    @Mappings({
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())"),
            @Mapping(target = "questionId", expression = "java(entity.getQuestion().getQuestionId())")
    })
    public abstract AnswerResponseDto toResponseDto(Answer entity);
    public abstract List<Answer> toEntityList(List<AnswerResponseDto> dtoList);
    public abstract List<AnswerResponseDto> toDtoList(List<Answer> entityList);

}


