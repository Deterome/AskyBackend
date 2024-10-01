package org.senla_project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class CollaborationCreateDto {

    private String collabName;
    private String createTime = LocalDate.now().toString();

}
