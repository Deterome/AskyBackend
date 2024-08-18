package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.Collaboration;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationDto {

    private UUID collabId;
    private String collabName;
    private Date createTime;

    public CollaborationDto(@NonNull Collaboration collab) {
        this.collabId = collab.getId();
        this.collabName = collab.getCollabName();
        this.createTime = collab.getCreateTime();
    }

}
