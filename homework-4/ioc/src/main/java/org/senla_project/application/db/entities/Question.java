package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Question extends Entity {

    private String header;
    private String body;
    private User author;
    private int interesting;
    private Date createTime;

    @Override
    public Question clone() {
        Question clone = (Question) super.clone();
        clone.header = header;
        clone.body = body;
        clone.author = author;
        clone.interesting = interesting;
        clone.createTime = createTime;
        return clone;
    }

}
