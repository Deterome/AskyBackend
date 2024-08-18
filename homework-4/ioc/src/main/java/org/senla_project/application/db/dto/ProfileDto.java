package org.senla_project.application.db.dto;

import lombok.*;
import org.senla_project.application.db.entities.Profile;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class ProfileDto {

    private UUID profileId;
    private String userName;
    private String bio;
    private String firstname;
    private String surname;
    private Date birthday;
    private String avatarUrl;
    private int rating;

    public ProfileDto(@NonNull Profile profile) {
        this.profileId = profile.getId();
        this.userName = profile.getUser().getNickname();
        this.bio = profile.getBio();
        this.firstname = profile.getFirstname();
        this.surname = profile.getSurname();
        this.birthday = profile.getBirthday();
        this.avatarUrl = profile.getAvatarUrl();
        this.rating = profile.getRating();
    }
}
