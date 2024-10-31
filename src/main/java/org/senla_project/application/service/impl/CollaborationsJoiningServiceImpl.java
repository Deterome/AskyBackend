package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningCreateDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningDeleteDto;
import org.senla_project.application.dto.collabJoin.CollaborationsJoiningResponseDto;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.mapper.CollabRoleMapper;
import org.senla_project.application.mapper.CollaborationsJoiningMapper;
import org.senla_project.application.repository.CollaborationsJoiningRepository;
import org.senla_project.application.service.CollaborationsJoiningService;
import org.senla_project.application.service.UserCollaborationCollabRoleService;
import org.senla_project.application.service.linker.CollaborationsJoiningLinkerService;
import org.senla_project.application.util.enums.DefaultCollabRole;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CollaborationsJoiningServiceImpl implements CollaborationsJoiningService {

    final private CollaborationsJoiningRepository collaborationsJoiningRepository;
    final private CollaborationsJoiningMapper collaborationsJoiningMapper;
    final private CollabRoleMapper collabRoleMapper;
    final private UserCollaborationCollabRoleService userCollabRoleService;
    final private CollaborationsJoiningLinkerService collabJoinLinkerService;

    private Set<CollabRole> getDefaultCollabRolesSetForUser() {
        return Stream.of(DefaultCollabRole.PARTICIPANT.toString())
                .map(collabRoleMapper::toCollabRoleFromName)
                .collect(Collectors.toSet());
    }

    @Transactional
    @Override
    public CollaborationsJoiningResponseDto create(@NonNull CollaborationsJoiningCreateDto element) {
        Set<CollabRole> defaultUserRolesInCollab = getDefaultCollabRolesSetForUser();
        for (var collabRole : defaultUserRolesInCollab) {
            userCollabRoleService.giveUserARoleInCollab(element.getUserName(), element.getCollabName(), collabRole.getCollabRoleName());
        }

        CollaborationsJoining collabJoin = collaborationsJoiningMapper.toCollabJoin(element);
        collabJoinLinkerService.linkCollabJoinWithUser(collabJoin);
        collabJoinLinkerService.linkCollabJoinWithCollab(collabJoin);
        return collaborationsJoiningMapper.toCollabJoinResponseDto(collaborationsJoiningRepository.save(collabJoin));
    }

    @Transactional
    @Override
    public void delete(@NonNull CollaborationsJoiningDeleteDto collabJoinDeleteDto) {
        collaborationsJoiningRepository.deleteByUsernameAndCollabName(collabJoinDeleteDto.getUsername(), collabJoinDeleteDto.getCollabName());
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
    @Override
    public CollaborationsJoiningResponseDto getByUsernameAndCollabName(String username, String collabName) throws EntityNotFoundException {
        return collaborationsJoiningRepository.findByUsernameAndCollabName(username, collabName)
                .map(collaborationsJoiningMapper::toCollabJoinResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration joining not found"));
    }

    @Transactional
    @Override
    public CollaborationsJoiningResponseDto joinAuthenticatedUserToCollab(String collabName) {
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CollaborationsJoiningCreateDto collaborationsJoiningCreateDto = CollaborationsJoiningCreateDto.builder()
                .collabName(collabName)
                .userName(authenticatedUser.getUsername())
                .build();
        return create(collaborationsJoiningCreateDto);
    }

}
