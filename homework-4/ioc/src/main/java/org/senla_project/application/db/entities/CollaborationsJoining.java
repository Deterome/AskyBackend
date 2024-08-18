package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoining extends Entity {

    private Collaboration collab;
    private User user;
    private Date joinDate;

    @Override
    public CollaborationsJoining clone() {
        CollaborationsJoining clone = (CollaborationsJoining) super.clone();
        clone.collab = collab;
        clone.user = user;
        clone.joinDate = joinDate;
        return clone;
    }

}
