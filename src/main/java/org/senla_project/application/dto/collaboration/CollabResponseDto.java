package org.senla_project.application.dto.collaboration;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollabResponseDto {

    private String collabId;
    private String collabName;
    private String createTime;

}
