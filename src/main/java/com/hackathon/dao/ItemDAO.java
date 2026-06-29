package com.hackathon.dao;

import com.hackathon.model.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Concrete DAO implementation for {@link Item} entity.
 * Abstracts concrete SQL commands away from GUI views and controllers.
 */
public class ItemDAO extends BaseDAO<Item, Long> {

    // RowMapper map definition mapping SQL rows to Item models.
    private final RowMapper<Item> itemMapper = rs -> new Item(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "SELECT * FROM items WHERE id = ?";
        return executeQuerySingle(sql, itemMapper, id);
    }

    @Override
    public List<Item> findAll() {
        String sql = "SELECT * FROM items ORDER BY id DESC";
        return executeQuery(sql, itemMapper);
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            // Insert operation
            String sql = "INSERT INTO items (name, description, price, created_at) VALUES (?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            Long generatedId = executeInsertAndGetGeneratedKey(sql, 
                    item.getName(), 
                    item.getDescription(), 
                    item.getPrice(), 
                    now
            );
            if (generatedId != null) {
                item.setId(generatedId);
                item.setCreatedAt(now);
            }
        } else {
            // Update operation
            String sql = "UPDATE items SET name = ?, description = ?, price = ? WHERE id = ?";
            executeUpdate(sql, 
                    item.getName(), 
                    item.getDescription(), 
                    item.getPrice(), 
                    item.getId()
            );
        }
        return item;
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM items WHERE id = ?";
        int affected = executeUpdate(sql, id);
        return affected > 0;
    }
}
