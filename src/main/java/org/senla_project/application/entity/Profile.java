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

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    User user;

    String bio;

    String firstname;

    String surname;

    LocalDate birthday;

    @Column(name = "avatar_url")
    String avatarUrl;

    int rating;

}
