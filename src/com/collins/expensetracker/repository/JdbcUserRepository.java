package com.collins.expensetracker.repository;

import com.collins.expensetracker.db.ConnectionFactory;
import com.collins.expensetracker.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements UserRepository{
    private final ConnectionFactory connectionFactory;

    public JdbcUserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public User save(User user) {

        // New user (id == 0) → INSERT
        if(user.getId() == 0){
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

            try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
                    ){
                ps.setString(1, user.getName());
                ps.setString(2,user.getEmail());

                int rows = ps.executeUpdate();
                if(rows==0){
                    throw new SQLException("Inserting user failed; no rows affected");
                }

                // Read generated id from database
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()){
                        int generatedId = rs.getInt(1);
                        user.setId(generatedId);
                    }
                }

                // created_at is handled by DB default; you can load it later if needed
                return user;
            }catch (SQLException e){
                throw new RuntimeException("Error saving user (insert) ",e);
            }
        }else{
            // Existing user (id != 0) → UPDATE
            String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
            try(Connection conn = connectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setInt(3, user.getId());

                ps.executeUpdate();

                return user;
            }catch (SQLException e){
                throw new RuntimeException("Error saving user (update)", e);
            }
        }
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT id, name, email, created_at FROM users WHERE id = ?";

        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    User user  = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    LocalDateTime createdAt = ts.toLocalDateTime();
                    user.setCreatedAt(createdAt);

                    return user;
                }else {
                    return null;
                }
        }catch (SQLException e){
            throw new RuntimeException("Error Retrieving User By id", e);
        }
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT id, name, email, created_at FROM users where email = ?";
        try(Connection conn = connectionFactory.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                user.setCreatedAt(createdAt);

                return user;
            }else{
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException("Error Retrieving User By email", e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                user.setCreatedAt(createdAt);

                users.add(user);
            }

            return users;
        }catch (SQLException e){
            throw new RuntimeException("Error Retrieving All Users", e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);

            int rows = ps.executeUpdate();
//            System.out.println(rows+" rows affected");
        }catch (SQLException e){
            throw new RuntimeException("Failed to Delete user by id", e);
        }
    }
}
