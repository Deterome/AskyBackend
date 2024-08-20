package org.senla_project.application.mapper;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.ProfileDto;
import org.senla_project.application.entity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Autowired
    private UserDao userDao;

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Profile toEntity(@NonNull ProfileDto dto) {
        return Profile.builder()
                .bio(dto.getBio())
                .avatarUrl(dto.getAvatarUrl())
                .birthday(LocalDate.parse(dto.getBirthday(), dateTimeFormatter_yyyy_MM_dd))
                .firstname(dto.getFirstname())
                .surname(dto.getSurname())
                .rating(dto.getRating())
                .user(userDao.findUserByNickname(dto.getUserName()))
            .build();
    }

    @Override
    public ProfileDto toDto(@NonNull Profile entity) {
        return ProfileDto.builder()
                .userName(entity.getUser().getNickname())
                .avatarUrl(entity.getAvatarUrl())
                .bio(entity.getBio())
                .birthday(entity.getBirthday().format(dateTimeFormatter_yyyy_MM_dd))
                .firstname(entity.getFirstname())
                .surname(entity.getSurname())
                .rating(entity.getRating())
            .build();
    }
}
