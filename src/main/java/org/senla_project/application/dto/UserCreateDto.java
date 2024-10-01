package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class UserCreateDto {

    private String nickname;
    private String password;

}