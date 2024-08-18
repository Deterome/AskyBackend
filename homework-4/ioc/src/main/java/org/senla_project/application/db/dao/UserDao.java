package org.senla_project.application.db.dao;

import org.senla_project.application.db.entities.User;

public class UserDao extends Dao<User> {
    public UserDao() {
        super(User.class);
    }

    public User findUserByNickname(String nickname) {
        for (var user: entities) {
            if (user.getNickname().equals(nickname)) {
                return user;
            }
        }
        return null;
    }
}