package com.collins.expensetracker.cli;

import com.collins.expensetracker.db.ConnectionFactory;
import com.collins.expensetracker.db.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class ExpenseTrackerApp {
    public static void main(String[] args) {

        DatabaseConfig config = new DatabaseConfig("jdbc:mysql://localhost:3306/expensetracker","root","Thisismanmad1@");
        ConnectionFactory connectionFactory = new ConnectionFactory(config);

        try(Connection conn = connectionFactory.getConnection()){
            System.out.println("Connection successful: "+!conn.isClosed());
        }catch (SQLException e){
            System.out.println("Fail to connect: "+e.getMessage());
        }
    }
}
