package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.entity.Question;

import java.util.List;
import java.util.UUID;

@Named("QuestionMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, UuidMapper.class})
public abstract class QuestionMapper {

    @Named("toQuestionFromId")
    public Question toQuestionFromId(UUID id) {
        return Question.builder()
                .questionId(id)
                .build();
    }

    @Mappings({
        @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "dto.authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"}),
        @Mapping(source = "id", target = "questionId")
    })
    public abstract Question toQuestion(UUID id, QuestionCreateDto dto);
    public Question toQuestion(QuestionCreateDto dto) {
        return toQuestion(null, dto);
    }
    @Mappings({
            @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "dto.authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"}),
    })
    public abstract Question toQuestion(QuestionResponseDto dto);
    @Mappings({
        @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "authorName", expression = "java(entity.getAuthor().getUsername())")
    })
    public abstract QuestionCreateDto toQuestionCreateDto(Question entity);
    @Mappings({
        @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "authorName", expression = "java(entity.getAuthor().getUsername())")
    })
    public abstract QuestionResponseDto toQuestionResponseDto(Question entity);
    public abstract List<Question> toQuestionList(List<QuestionResponseDto> dtoList);
    public abstract List<QuestionResponseDto> toQuestionDtoList(List<Question> entityList);

}
