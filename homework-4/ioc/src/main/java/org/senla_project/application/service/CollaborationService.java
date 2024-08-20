package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.mapper.CollaborationListMapper;
import org.senla_project.application.mapper.CollaborationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class CollaborationService implements ServiceInterface<CollaborationDto> {

    @Autowired
    private CollaborationDao collaborationDao;
    @Autowired
    private CollaborationMapper collaborationMapper;
    @Autowired
    private CollaborationListMapper collaborationListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<CollaborationDto> getAllElements() {
        return collaborationListMapper.toDtoList(collaborationDao.findAll());
    }

    @Override
    @Nullable
    public CollaborationDto getElementById(@NonNull UUID id) {
        Collaboration collaboration = collaborationDao.findById(id);
        if (collaboration == null) return null;
        return collaborationMapper.toDto(collaboration);
    }

    @Override
    public void addElement(@NonNull CollaborationDto element) {
        collaborationDao.create(collaborationMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationDto updatedElement) {
        collaborationDao.update(id, collaborationMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationDao.deleteById(id);
    }

    public UUID findCollabId(String collabName) {
        return collaborationDao.findCollabId(collabName);
    }

}
