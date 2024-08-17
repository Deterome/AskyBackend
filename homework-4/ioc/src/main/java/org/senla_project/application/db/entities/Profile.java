package org.senla_project.application.db.entities;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Profile extends Entity {

    private UUID profileId;
    private User user;
    private String bio;
    private String firstname;
    private String surname;
    private Date birthday;
    private String avatarUrl;
    private int rating;

}
