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
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Profile {

    @Column(name = "profile_id")
    @Id
    @GeneratedValue
    private UUID profileId;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    private String bio;

    private String firstname;

    private String surname;

    private LocalDate birthday;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private int rating;

}
