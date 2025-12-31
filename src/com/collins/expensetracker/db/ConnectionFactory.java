package com.collins.expensetracker.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private final DatabaseConfig config;

    public ConnectionFactory(DatabaseConfig config) {
        this.config = config;
    }

    public Connection getConnection() throws SQLException{

        return DriverManager.getConnection(config.getUrl(),config.getUserName(), config.getPassword());
    }
}
