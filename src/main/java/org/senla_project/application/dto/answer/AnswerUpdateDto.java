package org.senla_project.application.dto.answer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AnswerUpdateDto {

    private String answerId;
    private String body;
    private int usefulness;

}
