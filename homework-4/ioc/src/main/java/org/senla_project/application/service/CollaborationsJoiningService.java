package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.CollaborationDao;
import org.senla_project.application.db.dao.CollaborationsJoiningDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dto.CollaborationsJoiningDto;
import org.senla_project.application.db.entities.CollaborationsJoining;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CollaborationsJoiningService implements ServiceInterface<CollaborationsJoiningDto> {

    @Autowired
    private CollaborationsJoiningDao collaborationsJoiningDao;
    @Autowired
    private CollaborationDao collaborationDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<CollaborationsJoiningDto> getAllElements() {
        return collaborationsJoiningDao.findAll().stream().map(CollaborationsJoiningDto::new).toList();
    }

    @Override
    @Nullable
    public CollaborationsJoiningDto getElementById(@NonNull UUID id) {
        var collabJoin = collaborationsJoiningDao.findById(id);
        if (collabJoin == null) return null;
        return new CollaborationsJoiningDto(collabJoin);
    }

    @Override
    public void addElement(@NonNull CollaborationsJoiningDto element) {
        CollaborationsJoining newElement = CollaborationsJoining.builder()
                .collab(collaborationDao.findCollabByName(element.getCollabName()))
                .user(userDao.findUserByNickname(element.getUserName()))
                .joinDate(element.getJoinDate())
            .build();
        collaborationsJoiningDao.create(newElement);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationsJoiningDto updatedElement) {
        CollaborationsJoining updatedCollabJoin = CollaborationsJoining.builder()
                .collab(collaborationDao.findCollabByName(updatedElement.getCollabName()))
                .user(userDao.findUserByNickname(updatedElement.getUserName()))
                .joinDate(updatedElement.getJoinDate())
                .build();
        updatedCollabJoin.setId(updatedElement.getJoinId());
        collaborationsJoiningDao.update(id, updatedCollabJoin);
    }

    @Override
    public void deleteElement(@NonNull CollaborationsJoiningDto element) {
        collaborationsJoiningDao.deleteById(element.getJoinId());
    }

}
