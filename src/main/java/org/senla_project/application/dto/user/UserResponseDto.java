package org.senla_project.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class UserResponseDto {

    private String userId;
    private String username;
    private List<String> roles;

}