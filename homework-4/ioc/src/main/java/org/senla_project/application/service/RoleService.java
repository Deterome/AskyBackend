package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.dto.RoleDto;
import org.senla_project.application.entity.Role;
import org.senla_project.application.mapper.QuestionListMapper;
import org.senla_project.application.mapper.QuestionMapper;
import org.senla_project.application.mapper.RoleListMapper;
import org.senla_project.application.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RoleService implements ServiceInterface<RoleDto> {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleListMapper roleListMapper;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<RoleDto> getAllElements() {
        return roleListMapper.toDtoList(roleDao.findAll());
    }

    @Override
    @Nullable
    public RoleDto getElementById(@NonNull UUID id) {
        Role role = roleDao.findById(id);
        if (role == null) return null;
        return roleMapper.toDto(role);
    }

    @Override
    public void addElement(@NonNull RoleDto element) {
        roleDao.create(roleMapper.toEntity(element));
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull RoleDto updatedElement) {
        roleDao.update(id, roleMapper.toEntity(updatedElement));
    }

    @Override
    public void deleteElement(@NonNull UUID id) {
        roleDao.deleteById(id);
    }

    public UUID findRoleId(String roleName) {
        return roleDao.findRoleId(roleName);
    }

}
