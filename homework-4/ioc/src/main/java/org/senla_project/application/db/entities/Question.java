package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Question extends Entity {

    private UUID questionId;
    private String header;
    private String body;
    private User author;
    private int interesting;
    private Date createTime;

}
