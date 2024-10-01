package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class QuestionResponseDto {

    private String questionId;
    private String authorName;
    private String header;
    private String body;
    private int interesting;
    private String createTime;

}
