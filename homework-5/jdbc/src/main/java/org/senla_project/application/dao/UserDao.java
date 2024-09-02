package org.senla_project.application.dao;

import org.senla_project.application.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserDao extends DefaultDao<User> {
    Optional<User> findUserByNickname(String nickname);
}