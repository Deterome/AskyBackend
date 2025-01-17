package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "collabroles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollabRole {

    @Column(name = "collab_role_id")
    @Id
    @GeneratedValue
    private UUID collabRoleId;

    @Column(name = "collab_role_name")
    private String collabRoleName;

    @ManyToMany(mappedBy = "collabRoles", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Collaboration> collabs = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collabRole", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserCollaborationCollabRole> usersWithRoles;

}
