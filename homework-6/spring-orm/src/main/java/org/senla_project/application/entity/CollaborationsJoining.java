package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "collaborations_users")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationsJoining {

    @Id @GeneratedValue UUID joinId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "collab_id") Collaboration collab;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id") User user;
    LocalDate joinTime;

}
