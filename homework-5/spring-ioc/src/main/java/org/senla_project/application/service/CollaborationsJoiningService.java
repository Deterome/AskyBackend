package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.dao.CollaborationsJoiningDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.CollaborationsJoiningDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationsJoiningService implements ServiceInterface<CollaborationsJoiningDto, CollaborationsJoiningDto> {

    @Autowired
    private CollaborationsJoiningDao collaborationsJoiningDao;
    @Autowired
    private CollaborationDao collaborationDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CollaborationsJoiningMapper collaborationsJoiningMapper;

    @Override
    public void execute() {}

    @Transaction
    @Override
    public void addElement(@NonNull CollaborationsJoiningDto element) {
        collaborationsJoiningDao.create(collaborationsJoiningMapper.toEntity(element));
    }

    @Transaction
    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationsJoiningDto updatedElement) {
        collaborationsJoiningDao.update(id, collaborationsJoiningMapper.toEntity(updatedElement));
    }

    @Transaction
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationsJoiningDao.deleteById(id);
    }

    @Override
    public List<CollaborationsJoiningDto> getAllElements() {
        return collaborationsJoiningMapper.toDtoList(collaborationsJoiningDao.findAll());
    }

    @Override
    public Optional<CollaborationsJoiningDto> getElementById(@NonNull UUID id) {
        return collaborationsJoiningDao.findById(id)
                .map(collaborationsJoiningMapper::toDto);
    }

    public Optional<UUID> findCollaborationJoinId(String username, String collaboration) {
        return collaborationsJoiningDao.findCollaborationJoin(username, collaboration).map(Entity::getId);
    }

}
