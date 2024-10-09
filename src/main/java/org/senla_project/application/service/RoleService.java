package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService implements ServiceInterface<UUID, RoleCreateDto, RoleResponseDto> {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;

    @Transactional
    @Override
    public RoleResponseDto addElement(@NonNull RoleCreateDto element) {
        return roleMapper.toRoleResponseDto(roleRepository.create(roleMapper.toRole(element)));
    }

    @Transactional
    @Override
    public RoleResponseDto updateElement(@NonNull UUID id, @NonNull RoleCreateDto updatedElement) {
        return roleMapper.toRoleResponseDto(roleRepository.update(roleMapper.toRole(id, updatedElement)));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleResponseDto> findAllElements() throws EntityNotFoundException {
        var elements = roleMapper.toRoleDtoList(roleRepository.findAll());
        if (elements.isEmpty()) throw new EntityNotFoundException("Roles not found");
        return elements;
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponseDto findElementById(@NonNull UUID id) throws EntityNotFoundException {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Transactional(readOnly = true)
    public RoleResponseDto findRoleByName(String roleName) throws EntityNotFoundException {
        return roleRepository
                .findRoleByName(roleName)
                .map(roleMapper::toRoleResponseDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

}
