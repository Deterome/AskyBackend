package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.User;

import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class UserDto {

    private UUID userId;
    private String roleName;
    private String nickname;
    private String password;

    public UserDto(@NonNull User user) {
        this.userId = user.getId();
        this.roleName = user.getRole().getRoleName();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
    }
}