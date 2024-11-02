package org.senla_project.application.mapper;

import org.mapstruct.*;
import org.senla_project.application.dto.question.QuestionCreateDto;
import org.senla_project.application.dto.question.QuestionResponseDto;
import org.senla_project.application.dto.question.QuestionUpdateDto;
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
            @Mapping(source = "id", target = "questionId"),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "interesting", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "answers", ignore = true)
    })
    public abstract Question toQuestion(UUID id, QuestionCreateDto dto);

    @Mappings({
            @Mapping(target = "questionId", ignore = true),
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "interesting", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "answers", ignore = true)
    })
    public abstract Question toQuestion(QuestionCreateDto dto);

    @Mappings({
            @Mapping(target = "author", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "answers", ignore = true)
    })
    public abstract Question toQuestion(QuestionUpdateDto updateDto);

    @Mappings({
            @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "dto.authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"}),
            @Mapping(target = "answers", ignore = true)
    })
    public abstract Question toQuestion(QuestionResponseDto dto);

    public abstract QuestionCreateDto toQuestionCreateDto(Question entity);

    @Mappings({
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "authorName", expression = "java(entity.getAuthor().getUsername())")
    })
    public abstract QuestionResponseDto toQuestionResponseDto(Question entity);

    public abstract List<Question> toQuestionList(List<QuestionResponseDto> dtoList);

    public abstract List<QuestionResponseDto> toQuestionDtoList(List<Question> entityList);

}
