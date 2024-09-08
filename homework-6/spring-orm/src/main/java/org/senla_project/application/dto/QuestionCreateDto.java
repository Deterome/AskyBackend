package org.senla_project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class QuestionCreateDto {

    private String authorName;
    private String header;
    private String body;
    private int interesting;
    private String createTime;

}
