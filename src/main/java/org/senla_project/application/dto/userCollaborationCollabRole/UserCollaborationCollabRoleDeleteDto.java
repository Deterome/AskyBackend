package org.senla_project.application.dto.userCollaborationCollabRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserCollaborationCollabRoleDeleteDto {

    private String username;
    private String collabName;
    private String collabRoleName;

}
