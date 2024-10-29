package org.senla_project.application.entity;

import jakarta.persistence.*;
import lombok.*;
import org.senla_project.application.entity.identifiers.UserCollaborationCollabRoleId;

@Entity
@Table(name = "users_collaborations_collabroles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@IdClass(UserCollaborationCollabRoleId.class)
public class UserCollaborationCollabRole {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collab_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collaboration collab;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collab_role_id", insertable = false, updatable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CollabRole collabRole;

}

