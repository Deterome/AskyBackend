package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.CollaborationCreateDto;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaborationService implements ServiceInterface<UUID, CollaborationCreateDto, CollaborationResponseDto> {

    final private CollaborationRepository collaborationRepository;
    final private CollaborationMapper collaborationMapper;

    @Transactional
    @Override
    public CollaborationResponseDto create(@NonNull CollaborationCreateDto element) {
        return collaborationMapper.toCollabResponseDto(collaborationRepository.save(collaborationMapper.toCollab(element)));
    }

    @Transactional
    @Override
    public CollaborationResponseDto updateById(@NonNull UUID id, @NonNull CollaborationCreateDto updatedElement) throws EntityNotFoundException {
        if (!collaborationRepository.existsById(id)) throw new EntityNotFoundException("Collab not found");
        return collaborationMapper.toCollabResponseDto(collaborationRepository.save(collaborationMapper.toCollab(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        collaborationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CollaborationResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = collaborationRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Collaboration not found");
        return elements.map(collaborationMapper::toCollabResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public CollaborationResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return collaborationRepository.findById(id)
                .map(collaborationMapper::toCollabResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Transactional(readOnly = true)
    public CollaborationResponseDto getByCollabName(String collabName) throws EntityNotFoundException {
        return collaborationRepository.findByCollabName(collabName)
                .map(collaborationMapper::toCollabResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

}
