package com.hackathon.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain model class representing a catalog Item.
 * Follows core Object-Oriented standards: complete encapsulation and clear type separation.
 */
public class Item {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;

    // Default constructor
    public Item() {
    }

    // Constructor without ID (useful for creating new entities before database save)
    public Item(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // Full constructor
    public Item(Long id, String name, String description, BigDecimal price, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
    }

    // Getters and Setters (Encapsulation)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", createdAt=" + createdAt +
                '}';
    }
}
