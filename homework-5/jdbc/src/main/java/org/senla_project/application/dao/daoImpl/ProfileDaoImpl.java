package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
import org.senla_project.application.dao.ProfileDao;
import org.senla_project.application.entity.*;
import org.senla_project.application.util.connectionUtil.ConnectionHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProfileDaoImpl implements ProfileDao {

    @Autowired
    ConnectionHolder connectionHolder;

    final String selectRolesSqlQuery =
            "SELECT * FROM profiles AS p\n" +
            "   INNER JOIN users AS u\n" +
            "       ON u.user_id = p.user_id\n" +
            "   INNER JOIN roles AS r\n" +
            "       ON r.role_id = u.role_id\n";

    @Override
    public void create(@NonNull Profile entity) {
        String sqlQuery =
                "INSERT INTO profiles\n" +
                "   (profile_id, user_id, bio, firstname, surname, birthday, avatar_url, rating)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setObject(2, entity.getUser().getId());
            prepStmt.setString(3, entity.getBio());
            prepStmt.setString(4, entity.getFirstname());
            prepStmt.setString(5, entity.getSurname());
            prepStmt.setTimestamp(6, Timestamp.valueOf(entity.getBirthday().atStartOfDay()));
            prepStmt.setString(7, entity.getAvatarUrl());
            prepStmt.setInt(8, entity.getRating());

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull Profile updatedEntity) {
        String sqlQuery =
                "UPDATE profiles SET\n" +
                "   bio = ?\n" +
                "   firstname = ?\n" +
                "   surname = ?\n" +
                "   birthday = ?\n" +
                "   avatar_url = ?\n" +
                "   rating = ?\n" +
                "WHERE profile_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setString(1, updatedEntity.getBio());
            prepStmt.setString(2, updatedEntity.getFirstname());
            prepStmt.setString(3, updatedEntity.getSurname());
            prepStmt.setTimestamp(4, Timestamp.valueOf(updatedEntity.getBirthday().atStartOfDay()));
            prepStmt.setString(5, updatedEntity.getAvatarUrl());
            prepStmt.setInt(6, updatedEntity.getRating());
            prepStmt.setObject(7, updatedEntity.getId());


            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(@NonNull UUID id) {
        String sqlQuery =
                "DELETE FROM profiles\n" +
                "   WHERE profile_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Profile> findAll() {
        List<Profile> profiles = new ArrayList<>();
        try (Statement stmt = connectionHolder.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(selectRolesSqlQuery)){
            while (rs.next()) {
                profiles.add(makeEntityFromResultSet(rs));
            }
            return profiles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Profile> findById(@NonNull UUID id) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE profile_id = ?\n" +
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
    public Optional<Profile> findProfileByNickname(String nickname) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE u.username = ?\n" +
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

    private Profile makeEntityFromResultSet(ResultSet rs) {
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

            Profile profile = Profile.builder()
                    .user(user)
                    .bio(rs.getString("bio"))
                    .firstname(rs.getString("firstname"))
                    .surname(rs.getString("surname"))
                    .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                    .avatarUrl(rs.getString("avatar_url"))
                    .rating(rs.getInt("rating"))
                .build();
            profile.setId(UUID.fromString(rs.getString("profile_id")));

            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
