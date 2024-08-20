package org.senla_project.application.dao;

import org.senla_project.application.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends Dao<User> {
    public UserDao() {
        super(User.class);
    }

    public User findUserByNickname(String nickname) {
        for (User user: entities) {
            if (user.getNickname().equals(nickname)) {
                return user;
            }
        }
        return null;
    }
}