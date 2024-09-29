package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.InvalidRequestParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Service
public class CollaborationService implements ServiceInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    @Autowired
    private CollaborationRepository collaborationRepository;
    @Autowired
    private CollaborationMapper collaborationMapper;

    @Transactional
    @Override
    public CollaborationResponseDto addElement(@NonNull CollaborationCreateDto element) {
        return collaborationMapper.toResponseDto(collaborationRepository.create(collaborationMapper.toEntity(element)));
    }

    @Transactional
    @Override
    public CollaborationResponseDto updateElement(@NonNull UUID id, @NonNull CollaborationCreateDto updatedElement) {
        return collaborationMapper.toResponseDto(collaborationRepository.update(collaborationMapper.toEntity(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CollaborationResponseDto> getAllElements() throws EntityNotFoundException {
        var elements = collaborationMapper.toDtoList(collaborationRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Collaborations not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public CollaborationResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return collaborationRepository.findById(id)
                .map(collaborationMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Transactional(readOnly = true)
    public CollaborationResponseDto findCollabByName(String collabName) throws EntityNotFoundException {
        return collaborationRepository.findCollabByName(collabName)
                .map(collaborationMapper::toResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

}
