package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.CollaborationsJoiningResponseDto;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.mapper.UserMapper;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
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
    public CollaborationsJoiningResponseDto addElement(@NonNull CollaborationsJoiningCreateDto element) {
        return collaborationsJoiningMapper
                .toCollabJoinResponseDto(collaborationsJoiningRepository.create(
                        addDependenciesCollabJoin(collaborationsJoiningMapper.toCollabJoin(element))
                ));
    }

    @Transactional
    @Override
    public CollaborationsJoiningResponseDto updateElement(@NonNull UUID id, @NonNull CollaborationsJoiningCreateDto updatedElement) {
        return collaborationsJoiningMapper
                .toCollabJoinResponseDto(collaborationsJoiningRepository.update(
                        addDependenciesCollabJoin(collaborationsJoiningMapper.toCollabJoin(id, updatedElement))
                ));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        collaborationsJoiningRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CollaborationsJoiningResponseDto> findAllElements() throws EntityNotFoundException {
        var elements = collaborationsJoiningMapper.toCollabJoinDtoList(collaborationsJoiningRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Collaborations joining not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public CollaborationsJoiningResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return collaborationsJoiningRepository.findById(id)
                .map(collaborationsJoiningMapper::toCollabJoinResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration joining not found"));
    }

    @Transactional(readOnly = true)
    public CollaborationsJoiningResponseDto findCollabJoin(String username, String collaboration) throws EntityNotFoundException {
        return collaborationsJoiningRepository.findCollabJoin(username, collaboration)
                .map(collaborationsJoiningMapper::toCollabJoinResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration joining not found"));
    }

    @Transactional(readOnly = true)
    private CollaborationsJoining addDependenciesCollabJoin(CollaborationsJoining collaborationsJoining) {
        collaborationsJoining.setCollab(collaborationMapper.toCollab(
                collaborationService.findCollabByName(collaborationsJoining.getCollab().getCollabName())
        ));
        collaborationsJoining.setUser(userMapper.toUser(
                userService.findUserByUsername(collaborationsJoining.getUser().getUsername())
        ));

        return collaborationsJoining;
    }


}
