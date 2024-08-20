package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Question;

import java.util.List;

@Mapper(componentModel = "spring", uses = QuestionMapper.class)
public interface QuestionListMapper {

    List<Question> toEntityList(List<QuestionDto> dtoList);
    List<QuestionDto> toDtoList(List<Question> entityList);

}


