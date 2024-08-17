package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoining extends Entity {

    private UUID joinId;
    private Collaboration collab;
    private User user;
    private Date joinDate;

}
