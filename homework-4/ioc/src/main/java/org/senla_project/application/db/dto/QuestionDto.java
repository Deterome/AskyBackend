package org.senla_project.application.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class QuestionDto {

    private String header;
    private String body;
    private int interesting;
    private Date createTime;

}
