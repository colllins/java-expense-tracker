package com.collins.expensetracker.model;

import java.time.LocalDateTime;

/**
 * Represents an application user who owns categories and transactions.
 */
public class User {
    private int id;                                     // Database-generated primary key.
    private String name;                                // Display name of the user.
    private String email;                               // Unique email identifier.
    private LocalDateTime createdAt;                    // When the user record was created.

    /**
     * No-arg constructor used when building the object step by step
     * (for example, from a JDBC ResultSet).
     */
    public User() {}

    /**
     * Convenience constructor for creating a new user before saving to the database.
     * Assigns the current time as createdAt.
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
