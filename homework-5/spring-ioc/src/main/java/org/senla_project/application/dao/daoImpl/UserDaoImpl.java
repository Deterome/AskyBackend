package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
import org.senla_project.application.dao.UserDao;
import org.senla_project.application.entity.Role;
import org.senla_project.application.entity.User;
import org.senla_project.application.util.connectionUtil.ConnectionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    ConnectionHolder connectionHolder;

    final String selectUsersSqlQuery =
            "SELECT * FROM users AS u\n" +
            "   INNER JOIN roles AS r\n" +
            "       ON u.role_id = r.role_id\n";

    @Override
    public void create(@NonNull User entity) {
        String sqlQuery =
                "INSERT INTO users\n" +
                "   (user_id, role_id, username, hashed_password)\n" +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setObject(2, entity.getRole().getId());
            prepStmt.setString(3, entity.getNickname());
            prepStmt.setString(4, entity.getPassword());

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull User updatedEntity) {
        String sqlQuery =
                "UPDATE users SET\n" +
                "   role_id = ?\n" +
                "   username = ?\n" +
                "   hashed_password = ?\n" +
                "WHERE user_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, updatedEntity.getRole().getId());
            prepStmt.setString(2, updatedEntity.getNickname());
            prepStmt.setString(3, updatedEntity.getPassword());
            prepStmt.setObject(4, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(@NonNull UUID id) {
        String sqlQuery =
                "DELETE FROM users\n" +
                "   WHERE user_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Statement stmt = connectionHolder.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(selectUsersSqlQuery)){
            while (rs.next()) {
                users.add(makeEntityFromResultSet(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(@NonNull UUID id) {
        String sqlQuery = selectUsersSqlQuery +
                "WHERE user_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)){
            prepStmt.setObject(1, id);
            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(makeEntityFromResultSet(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findUserByNickname(String nickname) {
        String sqlQuery = selectUsersSqlQuery +
                "WHERE username = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)){
            prepStmt.setString(1, nickname);
            try (ResultSet rs = prepStmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(makeEntityFromResultSet(rs));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private User makeEntityFromResultSet(ResultSet rs) {
        try {
            Role userRole = Role.builder()
                    .roleName(rs.getString("role_name"))
                    .build();
            userRole.setId(UUID.fromString(rs.getString("role_id")));

            User user = User.builder()
                    .nickname(rs.getString("username"))
                    .role(userRole)
                    .password("hashed_password")
                    .build();
            user.setId(UUID.fromString(rs.getString("user_id")));

            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
