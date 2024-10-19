package org.senla_project.application.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.QuestionResponseDto;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService implements ServiceInterface<UUID, RoleCreateDto, RoleResponseDto> {

    final private RoleRepository roleRepository;
    final private RoleMapper roleMapper;

    @Transactional
    @Override
    public RoleResponseDto create(@NonNull RoleCreateDto element) {
        return roleMapper.toRoleResponseDto(roleRepository.save(roleMapper.toRole(element)));
    }

    @Transactional
    @Override
    public RoleResponseDto updateById(@NonNull UUID id, @NonNull RoleCreateDto updatedElement) throws EntityNotFoundException {
        if (!roleRepository.existsById(id)) throw new EntityNotFoundException("Role not found");
        return roleMapper.toRoleResponseDto(roleRepository.save(roleMapper.toRole(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteById(@NonNull UUID id) {
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoleResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = roleRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Role not found");
        return elements.map(roleMapper::toRoleResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        if (!roleRepository.existsById(id)) throw new EntityNotFoundException("Role not found");
        return roleRepository.findById(id)
                .map(roleMapper::toRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Transactional(readOnly = true)
    public RoleResponseDto getByRoleName(String roleName) throws EntityNotFoundException {
        return roleRepository
                .findByRoleName(roleName)
                .map(roleMapper::toRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

}
