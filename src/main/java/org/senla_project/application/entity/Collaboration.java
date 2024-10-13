package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "collaborations")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Collaboration {

    @Column(name = "collab_id")
    @Id
    @GeneratedValue
    private UUID collabId;

    @Column(name = "collab_name")
    private String collabName;

    @Column(name = "create_time")
    private LocalDate createTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collab", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollaborationsJoining> users;

}
