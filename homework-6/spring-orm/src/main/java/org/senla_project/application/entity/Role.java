package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Role {

    @Id @GeneratedValue UUID roleId;
    String roleName;

    @OneToMany(mappedBy = "role_id") Set<User> users;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    )
    Set<Role> roles;
}
