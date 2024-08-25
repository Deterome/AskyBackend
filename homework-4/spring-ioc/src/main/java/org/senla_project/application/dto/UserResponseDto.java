package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class UserResponseDto {

    private String roleName;
    private String nickname;

}