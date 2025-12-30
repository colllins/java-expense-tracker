package com.collins.expensetracker.model;

import java.time.LocalDateTime;

/**
 * Represents a spending/earning category (e.g. Groceries, Rent)
 * owned by a specific user.
 */
public class Category {
    private int id;                                 // Database-generated primary key.
    private int userId;                             // Owner user id (foreign key to User).
    private String catName;                         // Category name (e.g. "Groceries").
    private LocalDateTime createdAt;                // When the category was created.

    /**
     * No-arg constructor for building from JDBC ResultSet.
     */
    public Category() {}


    /**
     * Convenience constructor for creating a new category before saving.
     * Sets createdAt to now.
     */
    public Category(int userId, String catName) {
        this.userId = userId;
        this.catName = catName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", userId=" + userId +
                ", catName='" + catName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
