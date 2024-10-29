package org.senla_project.application.entity.identifiers;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.User;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCollaborationCollabRoleId implements Serializable {
    private UUID user;
    private UUID collab;
    private UUID collabRole;
}
