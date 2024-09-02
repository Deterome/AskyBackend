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

    @Id @GeneratedValue UUID collabId;
    String collabName;
    LocalDate createTime;

    @OneToMany(mappedBy = "collab_id", cascade = CascadeType.ALL) Set<CollaborationsJoining> users;

}
