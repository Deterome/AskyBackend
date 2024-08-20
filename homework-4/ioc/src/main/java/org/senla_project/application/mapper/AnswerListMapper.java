package org.senla_project.application.mapper;

import org.mapstruct.Mapper;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Answer;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AnswerMapper.class})
public interface AnswerListMapper {

    List<Answer> toEntityList(List<AnswerDto> dtoList);
    List<AnswerDto> toDtoList(List<Answer> entityList);

}


