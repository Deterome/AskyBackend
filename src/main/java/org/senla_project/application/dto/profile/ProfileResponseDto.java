package org.senla_project.application.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProfileResponseDto {

    private String profileId;
    private String username;
    private String bio;
    private String firstname;
    private String surname;
    private String birthday;
    private String avatarUrl;
    private int rating;

}
