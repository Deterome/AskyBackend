package org.senla_project.application.dto;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor @Data @Builder
public class ProfileDto {

    private UUID profileId;
    private String userName;
    private String bio;
    private String firstname;
    private String surname;
    private String birthday;
    private String avatarUrl;
    private int rating;

}
