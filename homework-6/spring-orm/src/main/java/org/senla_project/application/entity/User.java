package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class User {

    @Id @GeneratedValue UUID userId;
    @Column(name = "username") String nickname;
    @Column(name = "hashed_password") String password;

    @OneToOne(mappedBy = "user_id", cascade = CascadeType.ALL) Profile profile;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) Set<Question> questions;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) Set<Answer> answers;
    @OneToMany(mappedBy = "user_id", cascade = CascadeType.ALL) Set<CollaborationsJoining> collaborationsJoining;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    Set<Role> roles;

}