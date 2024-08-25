package org.senla_project.application.entity;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Answer extends Entity {

    private String body;
    private User author;
    private Question question;
    private int usefulness;
    private LocalDate createTime;

    @Override
    public Answer clone() {
        Answer clone = (Answer) super.clone();
        clone.body = body;
        clone.author = author;
        clone.question = question;
        clone.usefulness = usefulness;
        clone.createTime = createTime;
        return clone;
    }

}


