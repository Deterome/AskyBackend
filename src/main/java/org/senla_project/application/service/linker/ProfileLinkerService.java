package org.senla_project.application.service.linker;


import lombok.RequiredArgsConstructor;
import org.senla_project.application.entity.Profile;
import org.senla_project.application.entity.User;
import org.senla_project.application.repository.UserRepository;
import org.senla_project.application.util.exception.EntityLinkerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileLinkerService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Profile linkProfileWithUser(Profile profile) {
        User userInfo = profile.getUser();
        profile.setUser(userRepository.findByUsername(userInfo.getUsername())
                .orElseThrow(() -> new EntityLinkerException("Can't link a profile with user. User not found."))
        );

        return profile;
    }

}
