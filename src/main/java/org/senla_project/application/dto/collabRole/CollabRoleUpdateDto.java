package org.senla_project.application.dto.collabRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollabRoleUpdateDto {

    private String collabRoleId;
    private String collabRoleName;

}
