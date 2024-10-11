package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "collaborations")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Collaboration {

    @Column(name = "collab_id")
    @Id
    @GeneratedValue
    UUID collabId;

    @Column(name = "collab_name")
    String collabName;

    @Column(name = "create_time")
    LocalDate createTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collab", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<CollaborationsJoining> users;

}
