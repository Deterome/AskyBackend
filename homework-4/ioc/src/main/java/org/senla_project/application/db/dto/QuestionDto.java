package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.Question;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class QuestionDto {

    private UUID questionId;
    private String authorName;
    private String header;
    private String body;
    private int interesting;
    private Date createTime;

    public QuestionDto(@NonNull Question question) {
        this.questionId = question.getId();
        this.authorName = question.getAuthor().getNickname();
        this.header = question.getHeader();
        this.body = question.getBody();
        this.interesting = question.getInteresting();
        this.createTime = question.getCreateTime();
    }

}
