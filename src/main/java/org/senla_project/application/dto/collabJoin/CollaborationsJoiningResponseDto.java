package org.senla_project.application.dto.collabJoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollaborationsJoiningResponseDto {

    private String joinId;
    private String collabName;
    private String userName;
    private String joinDate;

}
