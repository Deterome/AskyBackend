package org.senla_project.application.dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class AnswerResponseDto {

    private String answerId;
    private String body;
    private int usefulness;
    private UUID questionId;
    private String authorName;
    private String createTime;

}


