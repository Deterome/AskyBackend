package org.senla_project.application.service.linker;


import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.CollabRole;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.User;
import org.senla_project.application.entity.UserCollaborationCollabRole;
import org.senla_project.application.repository.CollabRoleRepository;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCollaborationCollabroleLinkerService {

    private final UserRepository userRepository;
    private final CollaborationRepository collabRepository;
    private final CollabRoleRepository collabRoleRepository;

    @Transactional(readOnly = true)
    public UserCollaborationCollabRole linkUserCollabRoleWithUser(UserCollaborationCollabRole userCollabRole) {
        User userInfo = userCollabRole.getUser();
        userCollabRole.setUser(userRepository.findByUsername(userInfo.getUsername())
                .orElseThrow(() -> new EntityLinkerException("Can't link an user collab role with user. User not found."))
        );

        return userCollabRole;
    }

    @Transactional(readOnly = true)
    public UserCollaborationCollabRole linkUserCollabRoleWithCollab(UserCollaborationCollabRole userCollabRole) {
        Collaboration collabInfo = userCollabRole.getCollab();
        userCollabRole.setCollab(collabRepository.findByCollabName(collabInfo.getCollabName())
                .orElseThrow(() -> new EntityLinkerException("Can't link an user collab role with collab. Collab not found."))
        );

        return userCollabRole;
    }

    @Transactional(readOnly = true)
    public UserCollaborationCollabRole linkUserCollabRoleWithCollabRole(UserCollaborationCollabRole userCollabRole) {
        CollabRole collabRoleInfo = userCollabRole.getCollabRole();
        userCollabRole.setCollabRole(collabRoleRepository.findByCollabRoleName(collabRoleInfo.getCollabRoleName())
                .orElseThrow(() -> new EntityLinkerException("Can't link an user collab role with collab role. Collab role not found."))
        );

        return userCollabRole;
    }


}
