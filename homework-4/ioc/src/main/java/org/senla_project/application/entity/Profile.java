package org.senla_project.application.entity;

import lombok.*;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data @Builder
public class Profile extends Entity {

    private User user;
    private String bio;
    private String firstname;
    private String surname;
    private LocalDate birthday;
    private String avatarUrl;
    private int rating;

    @Override
    public Profile clone() {
        Profile clone = (Profile) super.clone();
        clone.user = user;
        clone.bio = bio;
        clone.firstname = firstname;
        clone.surname = surname;
        clone.birthday = birthday;
        clone.avatarUrl = avatarUrl;
        clone.rating = rating;
        return clone;
    }

}
