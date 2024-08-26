package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
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
public class CollaborationsJoiningDao implements org.senla_project.application.dao.CollaborationsJoiningDao {

    @Autowired
    ConnectionHolder connectionHolder;

    final String selectRolesSqlQuery =
            "SELECT * FROM collaborations_users AS cu\n" +
            "   INNER JOIN collaborations AS c\n" +
            "       ON c.collab_id = cu.collab_id\n" +
            "   INNER JOIN users AS u\n" +
            "       ON u.user_id = cu.user_id\n" +
            "   INNER JOIN roles AS r\n" +
            "       ON r.role_id = u.role_id\n";

    @Override
    public void create(@NonNull CollaborationsJoining entity) {
        String sqlQuery =
                "INSERT INTO collaborations_users\n" +
                        "   (join_id, collab_id, user_id, join_date)\n" +
                        "VALUES (?, ?, ?, ?)";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setObject(2, entity.getCollab().getId());
            prepStmt.setObject(3, entity.getUser().getId());
            prepStmt.setTimestamp(4, Timestamp.valueOf(entity.getJoinDate().atStartOfDay()));

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull CollaborationsJoining updatedEntity) {
        String sqlQuery =
                "UPDATE collaborations_users SET\n" +
                "   collab_id = ?\n" +
                "   user_id = ?\n" +
                "   join_date = ?\n" +
                "WHERE role_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, updatedEntity.getCollab().getId());
            prepStmt.setObject(2, updatedEntity.getUser().getId());
            prepStmt.setTimestamp(3, Timestamp.valueOf(updatedEntity.getJoinDate().atStartOfDay()));
            prepStmt.setObject(4, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(@NonNull UUID id) {
        String sqlQuery =
                "DELETE FROM collaborations_users\n" +
                "   WHERE join_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CollaborationsJoining> findAll() {
        List<CollaborationsJoining> joins = new ArrayList<>();
        try (Statement stmt = connectionHolder.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(selectRolesSqlQuery)){
            while (rs.next()) {
                joins.add(makeEntityFromResultSet(rs));
            }
            return joins;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CollaborationsJoining> findById(@NonNull UUID id) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE join_id = ?\n" +
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
    public Optional<CollaborationsJoining> findCollaborationJoin(String username, String collaboration) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE u.username = ?\n" +
                "   AND c.collab_name = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)){
            prepStmt.setString(1, username);
            prepStmt.setString(2, collaboration);
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

    private CollaborationsJoining makeEntityFromResultSet(ResultSet rs) {
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

            Collaboration collab = Collaboration.builder()
                    .collabName(rs.getString("collab_name"))
                    .createTime(rs.getTimestamp("create_time").toLocalDateTime().toLocalDate())
                    .build();
            collab.setId(UUID.fromString(rs.getString("collab_id")));

            CollaborationsJoining joining = CollaborationsJoining.builder()
                    .collab(collab)
                    .user(user)
                    .joinDate(rs.getTimestamp("join_date").toLocalDateTime().toLocalDate())
                    .build();
            joining.setId(UUID.fromString(rs.getString("join_id")));

            return joining;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
