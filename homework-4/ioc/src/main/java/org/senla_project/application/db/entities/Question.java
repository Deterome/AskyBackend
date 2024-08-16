package org.senla_project.application.db.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Question {

    private int questionId;
    private String header;
    private String body;
    private User author;
    private int interesting;
    private Date createTime;

}
