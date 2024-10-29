package org.senla_project.application.dto.collaboration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CollabCreateDto {

    private String collabName;
    private String createTime = LocalDate.now().toString();

}
