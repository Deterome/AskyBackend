package org.senla_project.application.db.entities;

import lombok.*;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class User extends Entity {

    private Role role;
    private String nickname;
    private String password;

    @Override
    public User clone() {
        User clone = (User) super.clone();
        clone.role = role;
        clone.nickname = nickname;
        clone.password = password;
        return clone;
    }

}