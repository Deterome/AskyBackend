package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
import org.senla_project.application.dao.RoleDao;
import org.senla_project.application.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RoleDaoImpl implements RoleDao {

    @Autowired
    Connection dbConn;

    final String selectRolesSqlQuery =
            "SELECT * FROM roles AS r\n";

    @Override
    public void create(@NonNull Role entity) {
        String sqlQuery =
                "INSERT INTO roles\n" +
                "   (role_id, role_name)\n" +
                "VALUES (?, ?)";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setString(2, entity.getRoleName());

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull Role updatedEntity) {
        String sqlQuery =
                "UPDATE roles SET\n" +
                "   role_name = ?\n" +
                "WHERE role_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setString(1, updatedEntity.getRoleName());
            prepStmt.setObject(2, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(@NonNull UUID id) {
        String sqlQuery =
                "DELETE FROM roles\n" +
                "   WHERE role_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Statement stmt = dbConn.createStatement();
             ResultSet rs = stmt.executeQuery(selectRolesSqlQuery)){
            while (rs.next()) {
                roles.add(makeEntityFromResultSet(rs));
            }
            return roles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Role> findById(@NonNull UUID id) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE role_id = ?\n" +
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
    public Optional<Role> findRoleByName(String roleName) {
        String sqlQuery = selectRolesSqlQuery +
                "WHERE r.role_name = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)){
            prepStmt.setString(1, roleName);
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

    private Role makeEntityFromResultSet(ResultSet rs) {
        try {
            Role role = Role.builder()
                    .roleName(rs.getString("role_name"))
                    .build();
            role.setId(UUID.fromString(rs.getString("role_id")));

            return role;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
