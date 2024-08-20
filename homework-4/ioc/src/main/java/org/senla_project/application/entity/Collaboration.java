package org.senla_project.application.entity;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data @Builder
public class Collaboration extends Entity {

    private String collabName;
    private LocalDate createTime;

    @Override
    public Collaboration clone() {
        Collaboration clone = (Collaboration) super.clone();
        clone.collabName = collabName;
        clone.createTime = createTime;
        return clone;
    }

}
