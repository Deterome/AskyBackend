package org.senla_project.application.db.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class User {

    private int userId;
    private Role role;
    private String nickname;
    private String password;

}