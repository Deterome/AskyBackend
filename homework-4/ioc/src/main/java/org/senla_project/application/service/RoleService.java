package org.senla_project.application.service;

import lombok.NonNull;
import org.senla_project.application.db.dao.RoleDao;
import org.senla_project.application.db.dao.QuestionDao;
import org.senla_project.application.db.dao.UserDao;
import org.senla_project.application.db.dto.RoleDto;
import org.senla_project.application.db.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RoleService implements ServiceInterface<RoleDto> {

    @Autowired
    private RoleDao roleDao;

    @Override
    public void execute() {}

    @Override
    @NonNull
    public List<RoleDto> getAllElements() {
        return roleDao.findAll().stream().map(RoleDto::new).toList();
    }

    @Override
    @Nullable
    public RoleDto getElementById(@NonNull UUID id) {
        var role = roleDao.findById(id);
        if (role == null) return null;
        return new RoleDto(role);
    }

    @Override
    public void addElement(@NonNull RoleDto element) {
        Role newElement = Role.builder()
                .roleName(element.getRoleName())
            .build();
        roleDao.create(newElement);
    }

    @Override
    public void updateElement(@NonNull UUID id, @NonNull RoleDto updatedElement) {
        Role updatedRole = Role.builder()
                .roleName(updatedElement.getRoleName())
            .build();
        updatedRole.setId(updatedElement.getRoleId());
        roleDao.update(id, updatedRole);
    }

    @Override
    public void deleteElement(@NonNull RoleDto element) {
        roleDao.deleteById(element.getRoleId());
    }

}
