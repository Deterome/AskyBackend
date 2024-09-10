package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.entity.Entity;
import org.senla_project.application.mapper.RoleMapper;
import org.senla_project.application.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RoleService implements ServiceInterface<RoleDto, RoleDto> {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public void execute() {}

    @Transaction
    @Override
    public void addElement(@NonNull RoleDto element) {
        roleDao.create(roleMapper.toEntity(element));
    }

    @Transaction
    @Override
    public void updateElement(@NonNull UUID id, @NonNull RoleDto updatedElement) {
        roleDao.update(id, roleMapper.toEntity(updatedElement));
    }

    @Transaction
    @Override
    public void deleteElement(@NonNull UUID id) {
        roleDao.deleteById(id);
    }

    @Transaction
    @Override
    public List<RoleDto> getAllElements() {
        return roleMapper.toDtoList(roleDao.findAll());
    }

    @Transaction
    @Override
    public Optional<RoleDto> getElementById(@NonNull UUID id) {
        return roleDao.findById(id)
                .map(roleMapper::toDto);
    }

    @Transaction
    public Optional<UUID> findRoleId(String roleName) {
        return roleDao.findRoleByName(roleName).map(Entity::getId);
    }

}
