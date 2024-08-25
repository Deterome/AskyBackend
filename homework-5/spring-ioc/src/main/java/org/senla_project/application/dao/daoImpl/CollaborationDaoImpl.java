package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
import org.senla_project.application.dao.CollaborationDao;
import org.senla_project.application.entity.Collaboration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CollaborationDaoImpl implements CollaborationDao {

    @Autowired
    Connection dbConn;

    final String selectRolesSqlQuery =
            "SELECT * FROM collaborations AS c\n";

    @Override
    public void create(@NonNull Collaboration entity) {
        String sqlQuery =
                "INSERT INTO collaborations\n" +
                "   (collab_id, collab_name, create_time)\n" +
                "VALUES (?, ?, ?)";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setString(2, entity.getCollabName());
            prepStmt.setTimestamp(3, Timestamp.valueOf(entity.getCreateTime().atStartOfDay()));

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull Collaboration updatedEntity) {
        String sqlQuery =
                "UPDATE collaborations SET\n" +
                "   collab_name = ?\n" +
                "   create_time = ?\n" +
                "WHERE role_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setString(1, updatedEntity.getCollabName());
            prepStmt.setTimestamp(2, Timestamp.valueOf(updatedEntity.getCreateTime().atStartOfDay()));
            prepStmt.setObject(3, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(@NonNull UUID id) {
        String sqlQuery =
                "DELETE FROM collaborations\n" +
                "   WHERE collab_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Collaboration> findAll() {
        List<Collaboration> collaborations = new ArrayList<>();
        try (Statement stmt = dbConn.createStatement();
             ResultSet rs = stmt.executeQuery(selectRolesSqlQuery)){
            while (rs.next()) {
                collaborations.add(makeEntityFromResultSet(rs));
            }
            return collaborations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Collaboration> findById(@NonNull UUID id) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE collab_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)){
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
    public Optional<Collaboration> findCollabByName(String collabName) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE collab_name = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)){
            prepStmt.setString(1, collabName);
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

    private Collaboration makeEntityFromResultSet(ResultSet rs) {
        try {
            Collaboration collab = Collaboration.builder()
                    .collabName(rs.getString("collab_name"))
                    .createTime(rs.getTimestamp("create_time").toLocalDateTime().toLocalDate())
                    .build();
            collab.setId(UUID.fromString(rs.getString("collab_id")));

            return collab;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
