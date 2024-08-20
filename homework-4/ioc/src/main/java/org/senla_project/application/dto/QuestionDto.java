package org.senla_project.application.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor @Data @Builder
public class QuestionDto {

    private UUID questionId;
    private String authorName;
    private String header;
    private String body;
    private int interesting;
    private String createTime;

}
