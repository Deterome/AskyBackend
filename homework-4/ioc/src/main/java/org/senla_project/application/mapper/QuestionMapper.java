package org.senla_project.application.mapper;

import org.senla_project.application.dto.QuestionDto;
import org.senla_project.application.entity.Question;

public interface QuestionMapper {

    Question toEntity(QuestionDto dto);
    QuestionDto toDto(Question entity);

}
