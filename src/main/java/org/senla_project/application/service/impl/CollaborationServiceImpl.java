package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.collabRole.CollabRoleResponseDto;
import org.senla_project.application.dto.collaboration.CollabCreateDto;
import org.senla_project.application.dto.collaboration.CollabDeleteDto;
import org.senla_project.application.dto.collaboration.CollabResponseDto;
import org.senla_project.application.dto.collaboration.CollabUpdateDto;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.mapper.CollabRoleMapper;
import org.senla_project.application.mapper.CollaborationMapper;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.service.CollabRoleService;
import org.senla_project.application.service.CollaborationService;
import org.senla_project.application.service.CollaborationsJoiningService;
import org.senla_project.application.service.UserCollaborationCollabRoleService;
import org.senla_project.application.service.linker.CollaborationLinkerService;
import org.senla_project.application.util.data.DefaultCollabRole;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.ForbiddenException;
import org.senla_project.application.util.security.AuthenticationManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CollaborationServiceImpl implements CollaborationService {

    final private CollaborationRepository collaborationRepository;
    final private CollaborationMapper collaborationMapper;
    final private CollabRoleService collabRoleService;
    final private CollabRoleMapper collabRoleMapper;
    final private CollaborationsJoiningService collabJoinService;
    final private UserCollaborationCollabRoleService userCollabRoleService;
    final private CollaborationLinkerService collabLinkerService;

    private Set<CollabRole> getDefaultCollabRolesSet() {
        return Stream.of(DefaultCollabRole.PARTICIPANT.toString(), DefaultCollabRole.CREATOR.toString())
                .map(collabRoleMapper::toCollabRoleFromName)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public CollabResponseDto create(@NonNull CollabCreateDto element) {
        Collaboration collaboration = collaborationMapper.toCollab(element);
        collaboration.setCollabRoles(getDefaultCollabRolesSet());
        collabLinkerService.linkCollaborationWithCollabRoles(collaboration);

        var response = collaborationMapper.toCollabResponseDto(collaborationRepository.save(collaboration));

        collabJoinService.joinAuthenticatedUserToCollab(response.getCollabName());
        userCollabRoleService.giveUserARoleInCollab(AuthenticationManager.getUsernameOfAuthenticatedUser(), response.getCollabName(), DefaultCollabRole.CREATOR.toString());

        return response;
    }

    @Transactional
    @Override
    public CollabResponseDto update(@NonNull CollabUpdateDto collabUpdateDto) throws EntityNotFoundException, HttpClientErrorException.Forbidden {
        var oldCollabInfo = collaborationRepository.findById(UUID.fromString(collabUpdateDto.getCollabId()));
        if (oldCollabInfo.isEmpty()) throw new EntityNotFoundException("Collaboration not found");
        if (isUserACreatorOfCollab(AuthenticationManager.getUsernameOfAuthenticatedUser(), oldCollabInfo.get().getCollabName())
                || AuthenticationManager.isAuthenticatedUserAnAdmin()) {
            Collaboration updatedCollab = collaborationMapper.toCollab(collabUpdateDto);
            return collaborationMapper.toCollabResponseDto(collaborationRepository.save(collaborationMapper.partialCollabToCollab(oldCollabInfo.get(), updatedCollab)));
        } else {
            throw new ForbiddenException(String.format("You are not an admin of '%s' collaboration!", oldCollabInfo.get().getCollabName()));
        }

    }

    @Transactional
    @Override
    public void delete(@NonNull CollabDeleteDto collabDeleteDto) {
        if (isUserACreatorOfCollab(AuthenticationManager.getUsernameOfAuthenticatedUser(), collabDeleteDto.getCollabName())
                || AuthenticationManager.isAuthenticatedUserAnAdmin()) {
            collaborationRepository.deleteByCollabName(collabDeleteDto.getCollabName());
        } else {
            throw new ForbiddenException(String.format("You are not an admin of '%s' collaboration!", collabDeleteDto.getCollabName()));
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CollabResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = collaborationRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Collaboration not found");
        return elements.map(collaborationMapper::toCollabResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public CollabResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return collaborationRepository.findById(id)
                .map(collaborationMapper::toCollabResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public CollabResponseDto getByCollabName(String collabName) throws EntityNotFoundException {
        return collaborationRepository.findByCollabName(collabName)
                .map(collaborationMapper::toCollabResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration not found"));
    }

    @Transactional
    @Override
    public boolean isUserACreatorOfCollab(String username, String collabName) {
        List<CollabRoleResponseDto> userRolesInCollab = collabRoleService.getUserRolesInCollab(username, collabName);
        return userRolesInCollab.stream()
                .anyMatch(userRoleInCollab -> userRoleInCollab.getCollabRoleName()
                        .equals(DefaultCollabRole.CREATOR.toString()));
    }

}
