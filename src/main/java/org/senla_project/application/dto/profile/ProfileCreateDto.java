package org.senla_project.application.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProfileCreateDto {

    private String bio;
    private String firstname;
    private String surname;
    private String birthday;
    private String avatarUrl;

}
