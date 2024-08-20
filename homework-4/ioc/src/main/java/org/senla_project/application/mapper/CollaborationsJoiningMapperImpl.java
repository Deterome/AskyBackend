package org.senla_project.application.mapper;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.AnswerDto;
import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.Answer;
import org.senla_project.application.entity.CollaborationsJoining;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CollaborationsJoiningMapperImpl implements CollaborationsJoiningMapper {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CollaborationDao collaborationDao;

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public CollaborationsJoining toEntity(@NonNull CollaborationsJoiningDto dto) {
        return CollaborationsJoining.builder()
                .joinDate(LocalDate.parse(dto.getJoinDate(), dateTimeFormatter_yyyy_MM_dd))
                .collab(collaborationDao.findCollabByName(dto.getCollabName()))
                .user(userDao.findUserByNickname(dto.getUserName()))
            .build();
    }

    @Override
    public CollaborationsJoiningDto toDto(@NonNull CollaborationsJoining entity) {
        return CollaborationsJoiningDto.builder()
                .joinDate(entity.getJoinDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .collabName(entity.getCollab().getCollabName())
                .userName(entity.getUser().getNickname())
            .build();
    }
}
