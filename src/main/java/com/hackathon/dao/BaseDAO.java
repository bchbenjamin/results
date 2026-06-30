package com.hackathon.dao;

import com.hackathon.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstract Base DAO providing reusable utility wrappers for boilerplate JDBC
 * operations.
 * Encourages Abstraction and DRY (Don't Repeat Yourself) design principles.
 *
 * @param <T>  The domain entity type
 * @param <ID> The entity's primary key identifier type
 */

public abstract class BaseDAO<T, ID> implements GenericDAO<T, ID> {

    /**
     * Functional interface to map a single row of a ResultSet to an object.
     */
    @FunctionalInterface
    protected interface RowMapper<R> {
        R mapRow(ResultSet rs) throws SQLException;
    }

    /**
     * Executes a SELECT query that returns a list of results.
     */
    protected List<T> executeQuery(String sql, RowMapper<T> mapper, Object... params) {
        List<T> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            handleException("Query execution failed: " + sql, e);
        }
        return results;
    }

    /**
     * Executes a SELECT query that is expected to return at most one result.
     */
    protected Optional<T> executeQuerySingle(String sql, RowMapper<T> mapper, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapper.mapRow(rs));
                }
            }
        } catch (SQLException e) {
            handleException("Single query execution failed: " + sql, e);
        }
        return Optional.empty();
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     *
     * @return the number of rows affected
     */
    protected int executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            setParameters(ps, params);
            return ps.executeUpdate();
        } catch (SQLException e) {
            handleException("Update execution failed: " + sql, e);
        }
        return 0;
    }

    /**
     * Executes an INSERT query and returns the generated primary key (Long).
     *
     * @return the generated ID key
     */
    protected Long executeInsertAndGetGeneratedKey(String sql, Object... params) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setParameters(ps, params);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting record failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Inserting record failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            handleException("Insert with generated keys failed: " + sql, e);
        }
        return null;
    }

    /**
     * Sets parameters on a PreparedStatement. Handles null inputs cleanly.
     */
    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param == null) {
                ps.setNull(i + 1, Types.NULL);
            } else if (param instanceof java.time.LocalDateTime) {
                ps.setTimestamp(i + 1, Timestamp.valueOf((java.time.LocalDateTime) param));
            } else {
                ps.setObject(i + 1, param);
            }
        }
    }

    /**
     * Centralized exception logging or handling. Can be extended to throw custom
     * runtime exceptions.
     */
    protected void handleException(String message, SQLException e) {
        System.err.println(message);
        e.printStackTrace();
        throw new RuntimeException(message, e);
    }
}
