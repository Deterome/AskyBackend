package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Profile {

    @Id @GeneratedValue UUID profileId;
    @Column(name = "user_id") @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) User user;
    String bio;
    String firstname;
    String surname;
    LocalDate birthday;
    String avatarUrl;
    int rating;

}
