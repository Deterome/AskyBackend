package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue
    UUID userId;

    @Column(name = "username")
    String username;

    @Column(name = "hashed_password")
    String password;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Profile profile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Question> questions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Answer> answers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<CollaborationsJoining> collaborationsJoining;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<Role> roles;

}