package org.senla_project.application.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class QuestionUpdateDto {

    private String questionId;
    private String header;
    private String body;
    private int interesting;

}
