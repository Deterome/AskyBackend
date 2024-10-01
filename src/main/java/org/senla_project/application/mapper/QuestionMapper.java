package org.senla_project.application.mapper;

import lombok.AllArgsConstructor;
import org.mapstruct.*;
import org.senla_project.application.dto.QuestionCreateDto;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Named("QuestionMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, UuidMapper.class})
public abstract class QuestionMapper {

    @Autowired
    private QuestionRepository questionRepository;

    @Named("toQuestionFromId")
    public Question toQuestionFromId(UUID id) {
        return questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Mappings({
        @Mapping(source = "dto.createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(source = "dto.authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"}),
        @Mapping(source = "id", target = "questionId")
    })
    public abstract Question toEntity(UUID id, QuestionCreateDto dto);
    public Question toEntity(QuestionCreateDto dto) {
        return toEntity(null, dto);
    }
    @Mappings({
        @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())")
    })
    public abstract QuestionCreateDto toCreateDto(Question entity);
    @Mappings({
        @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd"),
        @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())")
    })
    public abstract QuestionResponseDto toResponseDto(Question entity);
    public abstract List<Question> toEntityList(List<QuestionResponseDto> dtoList);
    public abstract List<QuestionResponseDto> toDtoList(List<Question> entityList);

}
