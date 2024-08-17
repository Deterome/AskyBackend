package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Answer extends Entity {

    private UUID answerId;
    private String body;
    private User author;
    private Question question;
    private int usefulness;
    private Date createTime;

}


