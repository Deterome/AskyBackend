package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@NamedEntityGraph(
        name = "profile-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("user")
        }
)
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Profile {

    @Column(name = "profile_id")
    @Id
    @GeneratedValue
    UUID profileId;

    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    User user;

    String bio;

    String firstname;

    String surname;

    LocalDate birthday;

    @Column(name = "avatar_url")
    String avatarUrl;

    int rating;

}
