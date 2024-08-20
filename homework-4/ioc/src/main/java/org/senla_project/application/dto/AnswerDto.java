package org.senla_project.application.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor @Data @Builder
public class AnswerDto {

    private String body;
    private int usefulness;
    private UUID questionId;
    private String authorName;
    private String createTime;

}


