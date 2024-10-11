package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "collaborations_users")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoining {

    @Column(name = "join_id")
    @Id
    @GeneratedValue
    UUID joinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collab_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Collaboration collab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    User user;

    @Column(name = "join_date")
    LocalDate joinDate;

}
