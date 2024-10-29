package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleCreateDto;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleDeleteDto;
import org.senla_project.application.dto.userCollaborationCollabRole.UserCollaborationCollabRoleResponseDto;
import org.senla_project.application.mapper.UserCollaborationCollabRoleMapper;
import org.senla_project.application.repository.UserCollaborationCollabRoleRepository;
import org.senla_project.application.service.CollabRoleService;
import org.senla_project.application.service.UserCollaborationCollabRoleService;
import org.senla_project.application.service.linker.UserCollaborationCollabroleLinkerService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.senla_project.application.util.exception.IncorrectDataEnteredException;
import org.senla_project.application.entity.identifiers.UserCollaborationCollabRoleId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCollaborationCollabRoleServiceImpl implements UserCollaborationCollabRoleService {

    private final UserCollaborationCollabRoleRepository userCollabRoleRepository;
    private final UserCollaborationCollabRoleMapper userCollabRoleMapper;
    private final CollabRoleService collabRoleService;
    private final UserCollaborationCollabroleLinkerService userCollabRoleLinkerService;

    @Override
    @Transactional(readOnly = true)
    public Page<UserCollaborationCollabRoleResponseDto> getAll(Pageable pageable) {
        var elements = userCollabRoleRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Users roles in collabs not found");
        return elements.map(userCollabRoleMapper::toUserCollaborationCollabRoleResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserCollaborationCollabRoleResponseDto getById(@NonNull UserCollaborationCollabRoleId id) {
        return userCollabRoleRepository.findById(id)
                .map(userCollabRoleMapper::toUserCollaborationCollabRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("User role in collab not found"));
    }

    @Override
    @Transactional
    public UserCollaborationCollabRoleResponseDto create(@NonNull UserCollaborationCollabRoleCreateDto createDto) {
        var userCollabRole = userCollabRoleMapper.toUserCollaborationCollabRole(createDto);
        userCollabRoleLinkerService.linkUserCollabRoleWithUser(userCollabRole);
        userCollabRoleLinkerService.linkUserCollabRoleWithCollab(userCollabRole);
        userCollabRoleLinkerService.linkUserCollabRoleWithCollabRole(userCollabRole);
        return userCollabRoleMapper.toUserCollaborationCollabRoleResponseDto(userCollabRoleRepository.save(userCollabRole));
    }

    @Override
    @Transactional
    public UserCollaborationCollabRoleResponseDto updateById(@NonNull UserCollaborationCollabRoleId id, @NonNull UserCollaborationCollabRoleCreateDto updatedElement) {
        if (!userCollabRoleRepository.existsById(id))
            throw new EntityNotFoundException("User role in collab not found");

        var userCollabRole = userCollabRoleMapper.toUserCollaborationCollabRole(id, updatedElement);
        userCollabRoleLinkerService.linkUserCollabRoleWithUser(userCollabRole);
        userCollabRoleLinkerService.linkUserCollabRoleWithCollab(userCollabRole);
        userCollabRoleLinkerService.linkUserCollabRoleWithCollabRole(userCollabRole);
        return userCollabRoleMapper.toUserCollaborationCollabRoleResponseDto(userCollabRoleRepository.save(userCollabRole));
    }

    @Transactional
    @Override
    public void giveUserARoleInCollab(String username, String collabName, String collabRole) {
        if (collabRoleService.hasCollabARole(collabName, collabRole)) {
            create(UserCollaborationCollabRoleCreateDto.builder()
                    .username(username)
                    .collabName(collabName)
                    .collabRoleName(collabRole)
                    .build());
        } else {
            throw new IncorrectDataEnteredException(String.format("Collaboration %s doesn't have '%s' role!", collabName, collabRole));
        }
    }

    @Override
    @Transactional
    public void delete(@NonNull UserCollaborationCollabRoleDeleteDto deleteDto) {
        userCollabRoleRepository.deleteByUsernameAndCollabNameAndCollabRoleName(deleteDto.getUsername(), deleteDto.getCollabName(), deleteDto.getCollabRoleName());
    }

}
