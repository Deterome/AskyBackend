package org.senla_project.application.service.linker;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.RoleRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserLinkerService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public User linkUserWithRoles(User user) {
        List<String> roleNameList = user.getRoles().stream()
                .map(Role::getRoleName).toList();
        Set<Role> roleSet = new HashSet<>();
        for (var roleName : roleNameList) {
            roleSet.add(roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new EntityLinkerException(String.format("Can't link an user with role. Role %s not found.", roleName)))
            );
        }
        user.setRoles(roleSet);

        return user;
    }

}
