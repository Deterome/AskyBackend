package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Collaboration extends Entity {

    private String collabName;
    private Date createTime;

    @Override
    public Collaboration clone() {
        Collaboration clone = (Collaboration) super.clone();
        clone.collabName = collabName;
        clone.createTime = createTime;
        return clone;
    }

}
