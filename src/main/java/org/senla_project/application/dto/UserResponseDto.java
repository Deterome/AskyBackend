package org.senla_project.application.dto;

import lombok.*;
import org.senla_project.application.entity.Role;

import java.util.Set;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class UserResponseDto {

    private String userId;
    private String nickname;

}