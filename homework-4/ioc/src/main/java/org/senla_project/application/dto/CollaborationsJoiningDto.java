package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoiningDto {

    private String collabName;
    private String userName;
    private String joinDate;

}
