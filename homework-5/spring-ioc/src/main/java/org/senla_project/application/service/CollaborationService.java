package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationService implements ServiceInterface<CollaborationDto, CollaborationDto> {

    @Autowired
    private CollaborationDao collaborationDao;
    @Autowired
    private CollaborationMapper collaborationMapper;

    @Override
    public void execute() {}

    @Transaction
    @Override
    public void addElement(@NonNull CollaborationDto element) {
        collaborationDao.create(collaborationMapper.toEntity(element));
    }

    @Transaction
    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationDto updatedElement) {
        collaborationDao.update(id, collaborationMapper.toEntity(updatedElement));
    }

    @Transaction
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationDao.deleteById(id);
    }

    @Override
    public List<CollaborationDto> getAllElements() {
        return collaborationMapper.toDtoList(collaborationDao.findAll());
    }

    @Override
    public Optional<CollaborationDto> getElementById(@NonNull UUID id) {
        return collaborationDao.findById(id)
                .map(collaborationMapper::toDto);
    }

    public Optional<UUID> findCollabId(String collabName) {
        return collaborationDao.findCollabByName(collabName).map(Entity::getId);
    }

}
