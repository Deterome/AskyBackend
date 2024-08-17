package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Collaboration extends Entity {

    private UUID collabId;
    private String collabName;
    private Date createTime;

}
