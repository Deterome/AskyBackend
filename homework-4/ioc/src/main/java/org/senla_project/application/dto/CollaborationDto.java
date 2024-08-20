package org.senla_project.application.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor @Data @Builder
public class CollaborationDto {

    private String collabName;
    private String createTime;

}
