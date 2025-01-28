package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.collabRole.CollabRoleCreateDto;
import org.senla_project.application.dto.collabRole.CollabRoleDeleteDto;
import org.senla_project.application.dto.collabRole.CollabRoleResponseDto;
import org.senla_project.application.dto.collabRole.CollabRoleUpdateDto;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.mapper.CollabRoleMapper;
import org.senla_project.application.repository.CollabRoleRepository;
import org.senla_project.application.service.CollabRoleService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollabRoleServiceImpl implements CollabRoleService {

    final private CollabRoleRepository collabRoleRepository;
    final private CollabRoleMapper collabRoleMapper;

    @Transactional
    @Override
    public CollabRoleResponseDto create(@NonNull CollabRoleCreateDto element) {
        return collabRoleMapper.toCollabRoleResponseDto(collabRoleRepository.save(collabRoleMapper.toCollabRole(element)));
    }

    @Transactional
    @Override
    public CollabRoleResponseDto update(@NonNull CollabRoleUpdateDto collabRoleUpdateDto) throws EntityNotFoundException {
        Optional<CollabRole> oldCollabRole = collabRoleRepository.findById(UUID.fromString(collabRoleUpdateDto.getCollabRoleId()));
        if (oldCollabRole.isEmpty()) throw new EntityNotFoundException("Collaboration role not found");
        CollabRole updatedCollabRole = collabRoleMapper.toCollabRole(collabRoleUpdateDto);
        return collabRoleMapper.toCollabRoleResponseDto(collabRoleRepository.save(collabRoleMapper.partialCollabRoleToCollabRole(oldCollabRole.get(), updatedCollabRole)));
    }

    @Transactional
    @Override
    public void delete(@NonNull CollabRoleDeleteDto roleDeleteDto) {
        collabRoleRepository.deleteByCollabRoleName(roleDeleteDto.getCollabRoleName());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CollabRoleResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = collabRoleRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Collaboration role not found");
        return elements.map(collabRoleMapper::toCollabRoleResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public CollabRoleResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        if (!collabRoleRepository.existsById(id)) throw new EntityNotFoundException("Collaboration role not found");
        return collabRoleRepository.findById(id)
                .map(collabRoleMapper::toCollabRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public CollabRoleResponseDto getByCollabRoleName(String collabRoleName) throws EntityNotFoundException {
        return collabRoleRepository
                .findByCollabRoleName(collabRoleName)
                .map(collabRoleMapper::toCollabRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Collaboration role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByCollabRoleName(String collabRoleName) {
        return collabRoleRepository.existsByCollabRoleName(collabRoleName);
    }

    @Transactional
    @Override
    public List<CollabRoleResponseDto> getUserRolesInCollab(String username, String collabName) {
        return collabRoleMapper.toCollabRoleResponseDtoList(
                collabRoleRepository.findByUsernameAndCollabName(username, collabName)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public boolean hasCollabARole(String collabName, String collabRoleName) {
        return collabRoleRepository.findByCollabName(collabName).stream()
                .anyMatch(collabRole -> collabRole.getCollabRoleName().equals(collabRoleName));
    }
}
