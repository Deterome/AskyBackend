package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.mapper.CollaborationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationService implements ServiceInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    @Autowired
    private CollaborationRepository collaborationRepository;
    @Autowired
    private CollaborationMapper collaborationMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull CollaborationCreateDto element) {
        collaborationRepository.create(collaborationMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull CollaborationCreateDto updatedElement) {
        collaborationRepository.update(collaborationMapper.toEntity(id, updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<CollaborationResponseDto> getAllElements() {
        return collaborationMapper.toDtoList(collaborationRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<CollaborationResponseDto> getElementById(@NonNull UUID id) {
        return collaborationRepository.findById(id)
                .map(collaborationMapper::toResponseDto);
    }

    @Transactional
    public Optional<UUID> findCollabId(String collabName) {
        return collaborationRepository.findCollabByName(collabName).map(Collaboration::getCollabId);
    }

}
