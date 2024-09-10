package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dto.RoleCreateDto;
import org.senla_project.application.dto.RoleResponseDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RoleService implements ServiceInterface<UUID, RoleCreateDto, RoleResponseDto> {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void execute() {}

    @Transactional
    @Override
    public void addElement(@NonNull RoleCreateDto element) {
        roleRepository.create(roleMapper.toEntity(element));
    }

    @Transactional
    @Override
    public void updateElement(@NonNull UUID id, @NonNull RoleCreateDto updatedElement) {
        roleRepository.update(roleMapper.toEntity(id, updatedElement));
    }

    @Transactional
    @Override
    public void deleteElement(@NonNull UUID id) {
        roleRepository.deleteById(id);
    }

    @Transactional
    @Override
    public List<RoleResponseDto> getAllElements() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Transactional
    @Override
    public Optional<RoleResponseDto> getElementById(@NonNull UUID id) {
        return roleRepository.findById(id)
                .map(roleMapper::toResponseDto);
    }

    @Transactional
    public Optional<UUID> findRoleId(String roleName) {
        return roleRepository.findRoleByName(roleName).map(Role::getRoleId);
    }

}
