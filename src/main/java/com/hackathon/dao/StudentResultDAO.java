package com.hackathon.dao;

import com.hackathon.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Concrete DAO implementation for the Student Result entity.
 * Fulfills the "Database Integration (CRUD)" requirement.
 */
public class StudentResultDAO extends BaseDAO<Student, String> {

    // RowMapper to convert ResultSet into Student objects
    private final RowMapper<Student> rowMapper = rs -> {
        Student s = new Student(
                rs.getString("usn"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDouble("sub1"),
                rs.getDouble("sub2"),
                rs.getDouble("sub3")
        );
        // The constructor auto-calculates total/avg/grade, but we explicitly set them
        // to exactly what's in the DB for integrity.
        s.setTotal(rs.getDouble("total"));
        s.setAverage(rs.getDouble("average"));
        s.setGrade(rs.getString("grade"));
        return s;
    };

    @Override
    public Optional<Student> findById(String usn) {
        String sql = "SELECT * FROM student_results WHERE usn = ?";
        return executeQuerySingle(sql, rowMapper, usn);
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM student_results ORDER BY usn ASC";
        return executeQuery(sql, rowMapper);
    }

    @Override
    public Student save(Student entity) {
        // We use MERGE so it acts as an UPSERT (Insert if new, Update if exists)
        String sql = "MERGE INTO student_results (usn, name, email, sub1, sub2, sub3, total, average, grade) KEY(usn) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql,
                entity.getUsn(),
                entity.getName(),
                entity.getEmail(),
                entity.getSub1(),
                entity.getSub2(),
                entity.getSub3(),
                entity.getTotal(),
                entity.getAverage(),
                entity.getGrade()
        );
        return entity;
    }

    @Override
    public boolean deleteById(String usn) {
        String sql = "DELETE FROM student_results WHERE usn = ?";
        return executeUpdate(sql, usn) > 0;
    }
}
