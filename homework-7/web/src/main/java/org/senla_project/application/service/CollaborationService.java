package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.mapper.CollaborationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CollaborationService implements ServiceInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    @Autowired
    private CollaborationRepository collaborationRepository;
    @Autowired
    private CollaborationMapper collaborationMapper;

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
    public Optional<CollaborationResponseDto> findElementById(@NonNull UUID id) {
        return collaborationRepository.findById(id)
                .map(collaborationMapper::toResponseDto);
    }

    @Transactional
    public Optional<CollaborationResponseDto> findCollab(String collabName) {
        return collaborationRepository.findCollabByName(collabName)
                .map(collaborationMapper::toResponseDto);
    }

}
