package org.senla_project.application.service.linker;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.repository.CollabRoleRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollaborationLinkerService {

    private final CollabRoleRepository collabRoleRepository;

    @Transactional(readOnly = true)
    public Collaboration linkCollaborationWithCollabRoles(Collaboration collab) {
        List<String> collabRolesNameList = collab.getCollabRoles().stream()
                .map(CollabRole::getCollabRoleName).toList();
        Set<CollabRole> collabRolesSet = new HashSet<>();
        for (var collabRoleName : collabRolesNameList) {
            collabRolesSet.add(collabRoleRepository.findByCollabRoleName(collabRoleName)
                    .orElseThrow(() -> new EntityLinkerException(String.format("Can't link a collaboration with collaboration roles. Collaboration role %s not found.", collabRoleName)))
            );
        }
        collab.setCollabRoles(collabRolesSet);

        return collab;
    }

}
