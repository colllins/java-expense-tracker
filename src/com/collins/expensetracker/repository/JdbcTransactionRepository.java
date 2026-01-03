package com.collins.expensetracker.repository;

import com.collins.expensetracker.db.ConnectionFactory;
import com.collins.expensetracker.model.Transaction;
import com.collins.expensetracker.model.TransactionType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransactionRepository implements TransactionRepository{
    private final ConnectionFactory connectionFactory;

    public JdbcTransactionRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Transaction save(Transaction tx) {
        if(tx.getId() == 0){
            String sql = "INSERT INTO transactions (user_id, category_id, type, amount, date, description) VALUES (?, ?, ?, ?, ?, ?)";

            try(Connection conn = connectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1, tx.getUserId());
                ps.setInt(2,tx.getCategoryId());
                ps.setString(3, String.valueOf(tx.getType()));
                ps.setBigDecimal(4,tx.getAmount());
                ps.setDate(5, Date.valueOf(tx.getDate()));
                ps.setString(6,tx.getDescription());

                int rows = ps.executeUpdate();
                if(rows == 0){
                    throw new RuntimeException("Inserting transaction failed; no rows affected");
                }

                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        int generatedKey = rs.getInt(1);
                        tx.setId(generatedKey);
                    }
                }
                return tx;
            }catch (SQLException e){
                throw new RuntimeException("Error saving transaction (insert)", e);
            }
        }else{
            String sql = "UPDATE transactions SET user_id = ?, category_id = ?, type = ?, amount = ?, date = ?, description = ? WHERE id = ?";

            try(Connection conn = connectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1,tx.getUserId());
                ps.setInt(2,tx.getCategoryId());
                ps.setString(3, String.valueOf(tx.getType()));
                ps.setBigDecimal(4,tx.getAmount());
                ps.setDate(5, Date.valueOf(tx.getDate()));
                ps.setString(6, tx.getDescription());
                ps.setInt(7,tx.getId());

                ps.executeUpdate();

                return tx;
            }catch (SQLException e){
                throw new RuntimeException("Error saving transaction (updating)", e);
            }
        }
    }

    @Override
    public Transaction findById(int id) {
        String sql = "SELECT id, user_id, category_id, type, amount, date, description, created_at, updated_at FROM transactions WHERE id = ?";
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Transaction tx  = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setUserId(rs.getInt("user_id"));
                tx.setCategoryId(rs.getInt("category_id"));
                tx.setType(TransactionType.valueOf(rs.getString("type")));
                tx.setAmount(rs.getBigDecimal("amount"));
                tx.setDate(rs.getDate("date").toLocalDate());
                tx.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                tx.setCreatedAt(createdAt);
                Timestamp udp = rs.getTimestamp("updated_at");
                LocalDateTime updatedAt = udp.toLocalDateTime();
                tx.setUpdatedAt(updatedAt);

                return tx;
            }else {
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException("Error Retrieving Transaction By id", e);
        }
    }

    @Override
    public List<Transaction> findByUserId(int userId) {
        String sql = "SELECT id, user_id, category_id, type, amount, date, description, created_at, updated_at FROM transactions WHERE user_id = ?";
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                Transaction tx  = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setUserId(rs.getInt("user_id"));
                tx.setCategoryId(rs.getInt("category_id"));
                tx.setType(TransactionType.valueOf(rs.getString("type")));
                tx.setAmount(rs.getBigDecimal("amount"));
                tx.setDate(rs.getDate("date").toLocalDate());
                tx.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                tx.setCreatedAt(createdAt);
                Timestamp udp = rs.getTimestamp("updated_at");
                LocalDateTime updatedAt = udp.toLocalDateTime();
                tx.setUpdatedAt(updatedAt);

                transactions.add(tx);
            }

            return transactions;
        }catch (SQLException e){
            throw new RuntimeException("Failed to retrieve transactions by userId", e);
        }
    }

    @Override
    public List<Transaction> findByUserIdAndDateRange(int userId, LocalDate from, LocalDate to) {
        String sql = "SELECT id, user_id, category_id, type, amount, date, description, created_at, updated_at FROM transactions " +
                "WHERE user_id = ? AND date BETWEEN ? AND ?";
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setDate(2, Date.valueOf(from));
            ps.setDate(3, Date.valueOf(to));
            ResultSet rs = ps.executeQuery();

            List<Transaction> transactions = new ArrayList<>();
            while (rs.next()) {
                Transaction tx  = new Transaction();
                tx.setId(rs.getInt("id"));
                tx.setUserId(rs.getInt("user_id"));
                tx.setCategoryId(rs.getInt("category_id"));
                tx.setType(TransactionType.valueOf(rs.getString("type")));
                tx.setAmount(rs.getBigDecimal("amount"));
                tx.setDate(rs.getDate("date").toLocalDate());
                tx.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                tx.setCreatedAt(createdAt);
                Timestamp udp = rs.getTimestamp("updated_at");
                LocalDateTime updatedAt = udp.toLocalDateTime();
                tx.setUpdatedAt(updatedAt);

                transactions.add(tx);
            }

            return transactions;
        }catch (SQLException e){
            throw new RuntimeException("Failed to retrieve transactions by userId and date range", e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);

            int rows = ps.executeUpdate();
//            System.out.println(rows+" rows affected");
        }catch (SQLException e){
            throw new RuntimeException("Failed to Delete transaction by id", e);
        }
    }
}
