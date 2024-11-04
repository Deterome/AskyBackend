package org.senla_project.application.dto.collaboration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollabUpdateDto {

    private String collabId;
    private String collabName;

}
