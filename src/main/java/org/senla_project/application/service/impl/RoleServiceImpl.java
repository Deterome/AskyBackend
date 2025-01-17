package org.senla_project.application.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.senla_project.application.dto.role.RoleCreateDto;
import org.senla_project.application.dto.role.RoleDeleteDto;
import org.senla_project.application.dto.role.RoleResponseDto;
import org.senla_project.application.dto.role.RoleUpdateDto;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.service.CrudService;
import org.senla_project.application.service.RoleService;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    final private RoleRepository roleRepository;
    final private RoleMapper roleMapper;

    @Transactional
    @Override
    public RoleResponseDto create(@NonNull RoleCreateDto element) {
        return roleMapper.toRoleResponseDto(roleRepository.save(roleMapper.toRole(element)));
    }

    @Transactional
    @Override
    public RoleResponseDto update(@NonNull RoleUpdateDto roleUpdateDto) throws EntityNotFoundException {
        if (!roleRepository.existsById(UUID.fromString(roleUpdateDto.getRoleId()))) throw new EntityNotFoundException("Role not found");
        return roleMapper.toRoleResponseDto(roleRepository.save(roleMapper.toRole(roleUpdateDto)));
    }

    @Transactional
    @Override
    public void delete(@NonNull RoleDeleteDto roleDeleteDto) {
        roleRepository.deleteByRoleName(roleDeleteDto.getRoleName());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoleResponseDto> getAll(Pageable pageable) throws EntityNotFoundException {
        var elements = roleRepository.findAll(pageable);
        if (elements.getTotalElements() == 0) throw new EntityNotFoundException("Roles not found");
        return elements.map(roleMapper::toRoleResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto getById(@NonNull UUID id) throws EntityNotFoundException {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto getByRoleName(String roleName) throws EntityNotFoundException {
        return roleRepository
                .findByRoleName(roleName)
                .map(roleMapper::toRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByRoleName(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

}
