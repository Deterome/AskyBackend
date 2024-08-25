package org.senla_project.application.dao.daoImpl;

import lombok.NonNull;
import org.senla_project.application.dao.AnswerDao;
import org.senla_project.application.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AnswerDaoImpl implements AnswerDao {

    @Autowired
    Connection dbConn;

    final String selectAnswersSqlQuery =
            "SELECT a.answer_id, a.body, a.usefulness, a.create_time,\n" +
            "   q.question_id, q.header AS question_header, q.body AS question_body, q.interesting AS question_interesting, q.create_time AS question_create_time,\n" +
            "   aAuthor.user_id AS answer_author_id, aAuthor.username AS answer_author_name,\n" +
            "   aAuthorRole.role_id AS answer_author_role_id, aAuthorRole.role_name AS answer_author_role,\n" +
            "   qAuthor.user_id AS question_author_id, qAuthor.username AS question_author_name,\n" +
            "   qAuthorRole.role_id AS question_author_role_id, qAuthorRole.role_name AS question_author_role\n" +
            "FROM answers AS a\n" +
            "   INNER JOIN questions AS q ON a.question_id = q.question_id\n" +
            "   INNER JOIN users AS aAuthor ON aAuthor.user_id = a.author\n" +
            "   INNER JOIN roles AS aAuthorRole ON aAuthorRole.role_id = aAuthor.role_id\n" +
            "   INNER JOIN users AS qAuthor ON qAuthor.user_id = q.author\n" +
            "   INNER JOIN roles AS qAuthorRole ON qAuthorRole.role_id = qAuthor.role_id\n";

    @Override
    public void create(@NonNull Answer entity) {
        String sqlQuery =
                "INSERT INTO answers\n" +
                "   (answer_id, body, author, question_id, usefulness, create_time)\n" +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, entity.getId());
            prepStmt.setString(2, entity.getBody());
            prepStmt.setObject(3, entity.getAuthor().getId());
            prepStmt.setObject(4, entity.getQuestion().getId());
            prepStmt.setInt(5, entity.getUsefulness());
            prepStmt.setTimestamp(6, Timestamp.valueOf(entity.getCreateTime().atStartOfDay()));

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(@NonNull UUID id, @NonNull Answer updatedEntity) {
        String sqlQuery =
                "UPDATE answers SET\n" +
                "   body = ?,\n" +
                "   author = ?,\n" +
                "   question_id = ?,\n" +
                "   usefulness = ?,\n" +
                "   create_time = ?\n" +
                "WHERE answer_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setString(1, updatedEntity.getBody());
            prepStmt.setObject(2, updatedEntity.getAuthor().getId());
            prepStmt.setObject(3, updatedEntity.getQuestion().getId());
            prepStmt.setInt(4, updatedEntity.getUsefulness());
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
                "DELETE FROM answers\n" +
                "   WHERE answer_id = ?\n" +
                "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)) {
            prepStmt.setObject(1, id);

            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Answer> findAll() {
        List<Answer> answers = new ArrayList<>();
        try (Statement stmt = dbConn.createStatement();
                ResultSet rs = stmt.executeQuery(selectAnswersSqlQuery)){
            while (rs.next()) {
                answers.add(makeEntityFromResultSet(rs));
            }
            return answers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Answer> findById(@NonNull UUID id) {
        String sqlQuery = selectAnswersSqlQuery +
                        "WHERE a.answer_id = ?\n" +
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
    public Optional<Answer> findAnswer(String authorName, UUID questionId, String body) {
        String sqlQuery = selectAnswersSqlQuery +
                        "WHERE answer_author_name = ?\n" +
                        "   AND question_id = ?\n" +
                        "   AND body = ?\n" +
                        "LIMIT 1";
        try (PreparedStatement prepStmt = dbConn.prepareStatement(sqlQuery)){
            prepStmt.setString(1, authorName);
            prepStmt.setObject(2, questionId);
            prepStmt.setString(3, body);
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

    private Answer makeEntityFromResultSet(ResultSet rs) {
        try {
            Role qstAuthorRole = Role.builder()
                    .roleName(rs.getString("question_author_role"))
                    .build();
            qstAuthorRole.setId(UUID.fromString(rs.getString("question_author_role_id")));

            User qstAuthor = User.builder()
                    .nickname(rs.getString("question_author_name"))
                    .role(qstAuthorRole)
                    .build();
            qstAuthor.setId(UUID.fromString(rs.getString("question_author_id")));

            Question qst = Question.builder()
                    .header(rs.getString("question_header"))
                    .body(rs.getString("question_body"))
                    .interesting(rs.getInt("question_interesting"))
                    .author(qstAuthor)
                    .createTime(rs.getTimestamp("question_create_time").toLocalDateTime().toLocalDate())
                    .build();
            qst.setId(UUID.fromString(rs.getString("question_id")));

            Role ansAuthorRole = Role.builder()
                    .roleName(rs.getString("answer_author_role"))
                    .build();
            ansAuthorRole.setId(UUID.fromString(rs.getString("answer_author_role_id")));

            User ansAuthor = User.builder()
                    .nickname(rs.getString("answer_author_name"))
                    .role(ansAuthorRole)
                    .build();
            ansAuthor.setId(UUID.fromString(rs.getString("answer_author_id")));

            Answer ans = Answer.builder()
                    .body(rs.getString("body"))
                    .question(qst)
                    .author(ansAuthor)
                    .usefulness(rs.getInt("usefulness"))
                    .createTime(rs.getTimestamp("create_time").toLocalDateTime().toLocalDate())
                .build();
            ans.setId(UUID.fromString(rs.getString("answer_id")));

            return ans;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}