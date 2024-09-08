package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "collaborations_users")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoining {

    @Column(name = "join_id") @Id @GeneratedValue UUID joinId;
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) @JoinColumn(name = "collab_id") Collaboration collab;
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) @JoinColumn(name = "user_id") User user;
    @Column(name = "join_date") LocalDate joinDate;

}
