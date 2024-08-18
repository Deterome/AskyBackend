package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.Answer;
import org.senla_project.application.db.entities.Question;
import org.senla_project.application.db.entities.User;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class AnswerDto {

    private UUID answerId;
    private String body;
    private int usefulness;
    private UUID questionId;
    private String authorName;
    private Date createTime;

    public AnswerDto(@NonNull Answer answer) {
        this.answerId = answer.getId();
        this.body = answer.getBody();
        this.usefulness = answer.getUsefulness();
        this.createTime = answer.getCreateTime();
        this.questionId = answer.getQuestion().getId();
        this.authorName = answer.getAuthor().getNickname();
    }

}


