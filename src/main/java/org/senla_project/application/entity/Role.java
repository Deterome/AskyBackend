package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Role {

    @Column(name = "role_id")
    @Id
    @GeneratedValue
    UUID roleId;

    @Column(name = "role_name")
    String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<User> users;
}
