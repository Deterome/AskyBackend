package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationResponseDto {

    private String collabId;
    private String collabName;
    private String createTime;

}
