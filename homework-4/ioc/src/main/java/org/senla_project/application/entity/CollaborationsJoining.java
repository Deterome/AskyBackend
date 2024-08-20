package org.senla_project.application.entity;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data @Builder
public class CollaborationsJoining extends Entity {

    private Collaboration collab;
    private User user;
    private LocalDate joinDate;

    @Override
    public CollaborationsJoining clone() {
        CollaborationsJoining clone = (CollaborationsJoining) super.clone();
        clone.collab = collab;
        clone.user = user;
        clone.joinDate = joinDate;
        return clone;
    }

}
