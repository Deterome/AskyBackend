package org.senla_project.application.service.linker;

import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.Collaboration;
import org.senla_project.application.entity.CollaborationsJoining;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.CollaborationRepository;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CollaborationsJoiningLinkerService {

    private final UserRepository userRepository;
    private final CollaborationRepository collabRepository;

    @Transactional(readOnly = true)
    public CollaborationsJoining linkCollabJoinWithUser(CollaborationsJoining collabJoin) {
        User userInfo = collabJoin.getUser();
        collabJoin.setUser(userRepository.findByUsername(userInfo.getUsername())
                .orElseThrow(() -> new EntityLinkerException("Can't link a collaboration join with user. User not found."))
        );

        return collabJoin;
    }

    @Transactional(readOnly = true)
    public CollaborationsJoining linkCollabJoinWithCollab(CollaborationsJoining collabJoin) {
        Collaboration collabInfo = collabJoin.getCollab();
        collabJoin.setCollab(collabRepository.findByCollabName(collabInfo.getCollabName())
                .orElseThrow(() -> new EntityLinkerException("Can't link a collaboration join with collaboration. Collaboration not found."))
        );

        return collabJoin;
    }

}
