package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class ProfileDto {

    private String userName;
    private String bio;
    private String firstname;
    private String surname;
    private String birthday;
    private String avatarUrl;
    private int rating;

}
