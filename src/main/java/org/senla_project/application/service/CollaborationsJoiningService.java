package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.CollaborationResponseDto;
import org.senla_project.application.dto.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.CollaborationsJoiningResponseDto;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollaborationsJoiningService implements ServiceInterface<UUID, CollaborationsJoiningCreateDto, CollaborationsJoiningResponseDto> {

    final private CollaborationsJoiningRepository collaborationsJoiningRepository;
    final private CollaborationsJoiningMapper collaborationsJoiningMapper;
    final private CollaborationService collaborationService;
    final private CollaborationMapper collaborationMapper;
    final private UserService userService;
    final private UserMapper userMapper;

    @Transactional
    @Override
    public CollaborationsJoiningResponseDto create(@NonNull CollaborationsJoiningCreateDto element) {
        return collaborationsJoiningMapper
                .toCollabJoinResponseDto(collaborationsJoiningRepository.save(
                        addDependenciesCollabJoin(collaborationsJoiningMapper.toCollabJoin(element))
                ));
    }

    @Transactional
    @Override
    public CollaborationsJoiningResponseDto updateById(@NonNull UUID id, @NonNull CollaborationsJoiningCreateDto updatedElement) throws EntityNotFoundException {
        if (!collaborationsJoiningRepository.existsById(id)) throw new EntityNotFoundException("Collaboration join not found");
        return collaborationsJoiningMapper
                .toCollabJoinResponseDto(collaborationsJoiningRepository.save(
                        addDependenciesCollabJoin(collaborationsJoiningMapper.toCollabJoin(id, updatedElement))
                ));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        collaborationsJoiningRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CollaborationsJoiningResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = collaborationsJoiningRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Collaboration joining not found");
        return elements.map(collaborationsJoiningMapper::toCollabJoinResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public CollaborationsJoiningResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return collaborationsJoiningRepository.findById(id)
                .map(collaborationsJoiningMapper::toCollabJoinResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration joining not found"));
    }

    @Transactional(readOnly = true)
    public CollaborationsJoiningResponseDto getByParams(String username, String collaboration) throws EntityNotFoundException {
        return collaborationsJoiningRepository.findByUsernameAndCollabName(username, collaboration)
                .map(collaborationsJoiningMapper::toCollabJoinResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration joining not found"));
    }

    @Transactional(readOnly = true)
    private CollaborationsJoining addDependenciesCollabJoin(CollaborationsJoining collaborationsJoining) {
        collaborationsJoining.setCollab(collaborationMapper.toCollab(
                collaborationService.getByCollabName(collaborationsJoining.getCollab().getCollabName())
        ));
        collaborationsJoining.setUser(userMapper.toUser(
                userService.getByUsername(collaborationsJoining.getUser().getUsername())
        ));

        return collaborationsJoining;
    }


}
