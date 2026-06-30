package com.hackathon.dao;

import com.hackathon.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Concrete DAO implementation for the Student and Marks entities.
 */
public class StudentResultDAO extends BaseDAO<Student, String> {

    private final RowMapper<Student> rowMapper = rs -> {
        Student s = new Student(
                rs.getString("USN"),
                rs.getString("Name"),
                rs.getInt("Batch"),
                rs.getInt("Team_No"),
                rs.getString("Topic"),
                rs.getDouble("DSA"),
                rs.getDouble("ADA"),
                rs.getDouble("DBMS"),
                rs.getDouble("Math"),
                rs.getDouble("Python"),
                rs.getDouble("Java"),
                rs.getDouble("SIP")
        );
        s.setTotalScore(rs.getDouble("Total_Score"));
        return s;
    };

    @Override
    public Optional<Student> findById(String usn) {
        String sql = "SELECT s.USN, s.Name, s.Batch, s.Team_No, s.Topic, " +
                     "m.DSA, m.ADA, m.DBMS, m.Math, m.Python, m.Java, m.SIP, m.Total_Score " +
                     "FROM Students s " +
                     "LEFT JOIN Marks m ON s.USN = m.USN " +
                     "WHERE s.USN = ?";
        return executeQuerySingle(sql, rowMapper, usn);
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT s.USN, s.Name, s.Batch, s.Team_No, s.Topic, " +
                     "m.DSA, m.ADA, m.DBMS, m.Math, m.Python, m.Java, m.SIP, m.Total_Score " +
                     "FROM Students s " +
                     "LEFT JOIN Marks m ON s.USN = m.USN " +
                     "ORDER BY m.Total_Score DESC, s.Name ASC"; 
        return executeQuery(sql, rowMapper);
    }

    @Override
    public Student save(Student entity) {
        String sqlStudent = "MERGE INTO Students (USN, Name, Batch, Team_No, Topic) KEY(USN) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sqlStudent, entity.getUsn(), entity.getName(), entity.getBatch(), entity.getTeamNo(), entity.getTopic());
        
        String sqlMarks = "MERGE INTO Marks (USN, DSA, ADA, DBMS, Math, Python, Java, SIP, Total_Score) KEY(USN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sqlMarks, entity.getUsn(), entity.getDsa(), entity.getAda(), entity.getDbms(), entity.getMath(), entity.getPython(), entity.getJavaMarks(), entity.getSip(), entity.getTotalScore());
        
        return entity;
    }

    @Override
    public boolean deleteById(String usn) {
        String sql = "DELETE FROM Students WHERE USN = ?";
        return executeUpdate(sql, usn) > 0;
    }
}
