package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.CollaborationDao;
import org.senla_project.application.db.dto.CollaborationDto;
import org.senla_project.application.db.entities.Collaboration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CollaborationService implements ServiceInterface<CollaborationDto> {

    @Autowired
    private CollaborationDao collaborationDao;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<CollaborationDto> getAllElements() {
        return collaborationDao.findAll().stream().map(CollaborationDto::new).toList();
    }

    @Override
    @Nullable
    public CollaborationDto getElementById(@NonNull UUID id) {
        var collab = collaborationDao.findById(id);
        if (collab == null) return null;
        return new CollaborationDto(collab);
    }

    @Override
    public void addElement(@NonNull CollaborationDto element) {
        Collaboration collaboration = Collaboration.builder()
                .collabName(element.getCollabName())
                .createTime(element.getCreateTime())
            .build();
        collaborationDao.create(collaboration);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationDto updatedElement) {
        Collaboration updatedCollaboration = Collaboration.builder()
                .collabName(updatedElement.getCollabName())
                .createTime(updatedElement.getCreateTime())
            .build();
        updatedCollaboration.setId(updatedElement.getCollabId());
        collaborationDao.update(id, updatedCollaboration);
    }

    @Override
    public void deleteElement(@NonNull CollaborationDto element) {
        collaborationDao.deleteById(element.getCollabId());
    }

}
