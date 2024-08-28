package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
import org.senla_project.application.dao.QuestionDao;
import org.senla_project.application.entity.Question;
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
public class QuestionDaoImpl implements QuestionDao {

    @Autowired
    ConnectionHolder connectionHolder;

    final String selectQuestionsSqlQuery =
            "SELECT * FROM questions AS q\n" +
            "   INNER JOIN users AS u\n" +
            "       ON u.user_id = q.author\n" +
            "   INNER JOIN roles AS r\n" +
            "       ON u.role_id = r.role_id\n";

    @Override
    public void create(@NonNull Question entity) {
        String sqlQuery =
                "INSERT INTO questions\n" +
                "   (question_id, header, body, author, interesting, create_time)\n" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setString(2, entity.getHeader());
            prepStmt.setString(3, entity.getBody());
            prepStmt.setObject(4, entity.getAuthor().getId());
            prepStmt.setInt(5, entity.getInteresting());
            prepStmt.setTimestamp(6, Timestamp.valueOf(entity.getCreateTime().atStartOfDay()));

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull Question updatedEntity) {
        String sqlQuery =
                "UPDATE questions SET\n" +
                "   header = ?,\n" +
                "   body = ?,\n" +
                "   author = ?,\n" +
                "   interesting = ?,\n" +
                "   create_time = ?\n" +
                "WHERE question_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setString(1, updatedEntity.getHeader());
            prepStmt.setString(2, updatedEntity.getBody());
            prepStmt.setObject(3, updatedEntity.getAuthor().getId());
            prepStmt.setInt(4, updatedEntity.getInteresting());
            prepStmt.setTimestamp(5, Timestamp.valueOf(updatedEntity.getCreateTime().atStartOfDay()));
            prepStmt.setObject(6, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(@NonNull UUID id) {
        String sqlQuery =
                "DELETE FROM questions\n" +
                "   WHERE question_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        try (Statement stmt = connectionHolder.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(selectQuestionsSqlQuery)){
            while (rs.next()) {
                questions.add(makeEntityFromResultSet(rs));
            }
            return questions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Question> findById(@NonNull UUID id) {
        String sqlQuery = selectQuestionsSqlQuery +
                "WHERE question_id = ?\n" +
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
    public Optional<Question> findQuestion(String header, String body, String authorName) {
        String sqlQuery = selectQuestionsSqlQuery +
                "WHERE q.header = ?\n" +
                "   AND q.body = ?\n" +
                "   AND u.username = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = connectionHolder.getConnection().prepareStatement(sqlQuery)){
            prepStmt.setString(1, header);
            prepStmt.setString(2, body);
            prepStmt.setString(3, authorName);
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

    private Question makeEntityFromResultSet(ResultSet rs) {
        try {
            Role userRole = Role.builder()
                    .roleName(rs.getString("role_name"))
                    .build();
            userRole.setId(UUID.fromString(rs.getString("role_id")));

            User author = User.builder()
                    .nickname(rs.getString("username"))
                    .role(userRole)
                    .password("hashed_password")
                    .build();
            author.setId(UUID.fromString(rs.getString("user_id")));

            Question question = Question.builder()
                    .header(rs.getString("header"))
                    .body(rs.getString("body"))
                    .author(author)
                    .interesting(rs.getInt("interesting"))
                    .createTime(rs.getTimestamp("create_time").toLocalDateTime().toLocalDate())
                    .build();
            question.setId(UUID.fromString(rs.getString("question_id")));

            return question;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
