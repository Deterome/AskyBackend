package org.senla_project.application.dto;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class ProfileResponseDto {

    private String profileId;
    private String userName;
    private String bio;
    private String firstname;
    private String surname;
    private String birthday;
    private String avatarUrl;
    private int rating;

}
