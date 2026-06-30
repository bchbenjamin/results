package com.hackathon.dao;

import com.hackathon.model.Evaluator;
import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.hackathon.db.DatabaseConnection;

public class EvaluatorDAO extends BaseDAO<Evaluator, String> {

    private final RowMapper<Evaluator> rowMapper = rs -> {
        return new Evaluator(
                rs.getString("Evaluator_ID"),
                rs.getString("Name"),
                rs.getString("Core_Subject")
        );
    };

    @Override
    public Optional<Evaluator> findById(String id) {
        String sql = "SELECT * FROM Evaluators WHERE Evaluator_ID = ?";
        return executeQuerySingle(sql, rowMapper, id);
    }

    @Override
    public List<Evaluator> findAll() {
        String sql = "SELECT * FROM Evaluators";
        return executeQuery(sql, rowMapper);
    }

    @Override
    public Evaluator save(Evaluator entity) {
        String sql = "INSERT INTO Evaluators (Evaluator_ID, Name, Core_Subject) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE Name=VALUES(Name), Core_Subject=VALUES(Core_Subject)";
        executeUpdate(sql, entity.getEvaluatorId(), entity.getName(), entity.getCoreSubject());
        return entity;
    }

    @Override
    public boolean deleteById(String id) {
        String sql = "DELETE FROM Evaluators WHERE Evaluator_ID = ?";
        return executeUpdate(sql, id) > 0;
    }
    
    /**
     * Checks if the evaluator is assigned as the SIP Mentor for the given topic.
     */
    public boolean isSipMentorForTopic(String evaluatorId, String topic) {
        String sql = "SELECT COUNT(*) AS count FROM SIP_Mapping WHERE Evaluator_ID = ? AND Topic = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, evaluatorId);
            ps.setString(2, topic);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
