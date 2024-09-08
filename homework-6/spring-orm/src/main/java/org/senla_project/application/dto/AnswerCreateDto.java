package org.senla_project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class AnswerCreateDto {

    private String body;
    private int usefulness;
    private UUID questionId;
    private String authorName;
    private String createTime;

}


