package org.senla_project.application.mapper;

import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.entity.Answer;

public interface AnswerMapper {

    Answer toEntity(AnswerDto dto);
    AnswerDto toDto(Answer entity);

}


