package org.senla_project.application.db.entities;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class User extends Entity {

    private UUID userId;
    private Role role;
    private String nickname;
    private String password;

}