package org.senla_project.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserUpdateDto {

    private String userId;
    private String username;
    private String password;
    private List<String> roles;

}
