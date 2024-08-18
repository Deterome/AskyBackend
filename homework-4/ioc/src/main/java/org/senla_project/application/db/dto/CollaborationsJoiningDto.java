package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.CollaborationsJoining;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoiningDto {

    private UUID joinId;
    private String collabName;
    private String userName;
    private Date joinDate;

    public CollaborationsJoiningDto(@NonNull CollaborationsJoining collaborationsJoining) {
        this.joinId = collaborationsJoining.getId();
        this.collabName = collaborationsJoining.getCollab().getCollabName();
        this.userName = collaborationsJoining.getUser().getNickname();
        this.joinDate = collaborationsJoining.getJoinDate();
    }
}
