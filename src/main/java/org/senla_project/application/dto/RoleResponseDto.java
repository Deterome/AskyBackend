package org.senla_project.application.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class RoleResponseDto {

    private String roleId;
    private String roleName;
    @Builder.Default
    private List<String> users = new ArrayList<>();

}
