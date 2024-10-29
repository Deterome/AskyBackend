package org.senla_project.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCreateDto {

    private String username;
    private String password;
    @Builder.Default
    private List<String> roles = new ArrayList<>();

}