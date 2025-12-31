package com.collins.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a single money movement (income or expense)
 * for a given user and category.
 */
public class Transaction {
    private int id;                                         // Database-generated primary key.
    private int userId;                                     // Owner user id (foreign key to User).
    private int categoryId;                                      // Category id (foreign key to Category).
    private TransactionType type;                           // EXPENSE or INCOME.
    private BigDecimal amount;                              // Money amount.
    private LocalDate date;                                 // Effective transaction date.
    private String description;                             // Optional details ("Uber to work").
    private LocalDateTime createdAt;                        // When the transaction was created.
    private LocalDateTime updatedAt;                        // When it was last modified.


    /**
     * No-arg constructor for building from JDBC ResultSet.
     */
    public Transaction() {}


    /**
     * Convenience constructor for creating a new transaction before saving.
     * Sets createdAt/updatedAt to now.
     */
    public Transaction(int userId, int categoryId, TransactionType type, BigDecimal amount, LocalDate date, String description) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int catId) {
        this.categoryId = catId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", userId=" + userId +
                ", catId=" + categoryId +
                ", type=" + type +
                ", amount=" + amount +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
