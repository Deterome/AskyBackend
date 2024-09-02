package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.senla_project.application.repository.QuestionRepository;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Question;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Named("QuestionMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public abstract class QuestionMapper {

    @Autowired
    private QuestionRepository questionRepository;

    @Named("toQuestionFromId")
    public Question toQuestionFromId(UUID id) {
        return questionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    @Mapping(source = "authorName", target = "author", qualifiedByName = {"UserMapper", "toUserFromName"})
    public abstract Question toEntity(QuestionDto dto);
    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "authorName", expression = "java(entity.getAuthor().getNickname())")
    public abstract QuestionDto toDto(Question entity);
    public abstract List<Question> toEntityList(List<QuestionDto> dtoList);
    public abstract List<QuestionDto> toDtoList(List<Question> entityList);

}
