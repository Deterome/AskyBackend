package org.senla_project.application.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor @Data @Builder
public class UserDto {

    private String roleName;
    private String nickname;
    private String password;

}