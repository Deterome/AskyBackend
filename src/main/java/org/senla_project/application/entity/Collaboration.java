package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    @CreatedDate
    private LocalDate createTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collab", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollaborationsJoining> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "collab", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<UserCollaborationCollabRole> usersRoles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "collaborations_collabroles",
            joinColumns = @JoinColumn(name = "collab_id"),
            inverseJoinColumns = @JoinColumn(name = "collab_role_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CollabRole> collabRoles;

}
