package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.dto.CollaborationDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.CollaborationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationService implements ServiceInterface<CollaborationDto, CollaborationDto> {

    @Autowired
    private CollaborationRepository collaborationRepository;
    @Autowired
    private CollaborationMapper collaborationMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull CollaborationDto element) {
        collaborationRepository.create(collaborationMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationDto updatedElement) {
        collaborationRepository.update(id, collaborationMapper.toEntity(updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<CollaborationDto> getAllElements() {
        return collaborationMapper.toDtoList(collaborationRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<CollaborationDto> getElementById(@NonNull UUID id) {
        return collaborationRepository.findById(id)
                .map(collaborationMapper::toDto);
    }

    @Transactional
    public Optional<UUID> findCollabId(String collabName) {
        return collaborationRepository.findCollabByName(collabName).map(Entity::getId);
    }

}
