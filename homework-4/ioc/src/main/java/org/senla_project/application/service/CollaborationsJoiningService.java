package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.dao.CollaborationsJoiningDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollaborationsJoiningListMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    @Autowired
    private CollaborationsJoiningMapper collaborationsJoiningMapper;
    @Autowired
    private CollaborationsJoiningListMapper collaborationsJoiningListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<CollaborationsJoiningDto> getAllElements() {
        return collaborationsJoiningListMapper.toDtoList(collaborationsJoiningDao.findAll());
    }

    @Override
    @Nullable
    public CollaborationsJoiningDto getElementById(@NonNull UUID id) {
        CollaborationsJoining collabJoin = collaborationsJoiningDao.findById(id);
        if (collabJoin == null) return null;
        return collaborationsJoiningMapper.toDto(collabJoin);
    }

    @Override
    public void addElement(@NonNull CollaborationsJoiningDto element) {
        collaborationsJoiningDao.create(collaborationsJoiningMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationsJoiningDto updatedElement) {
        collaborationsJoiningDao.update(id, collaborationsJoiningMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull CollaborationsJoiningDto element) {
        collaborationsJoiningDao.deleteById(element.getJoinId());
    }

}
