package com.collins.expensetracker.repository;

import com.collins.expensetracker.db.ConnectionFactory;
import com.collins.expensetracker.model.Category;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcCategoryRepository implements CategoryRepository{
    private final ConnectionFactory connectionFactory;

    public JdbcCategoryRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Category save(Category category) {
        if(category.getId() == 0){
            String sql = "INSERT INTO categories (user_id, catName) VALUES (?, ?)";

            try(Connection conn = connectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setInt(1,category.getUserId());
                ps.setString(2,category.getCatName());

                 int rows = ps.executeUpdate();
                 if(rows == 0){
                     throw new RuntimeException("Inserting category failed; no rows affected");
                 }

                 try(ResultSet rs = ps.getGeneratedKeys()){
                     if(rs.next()){
                         int generatedKey = rs.getInt(1);
                         category.setId(generatedKey);
                     }
                 }
                 return category;
            }catch (SQLException e){
                throw new RuntimeException("Error saving category (insert)", e);
            }
        }else{
            String sql = "UPDATE categories SET user_id = ?, catName = ? WHERE id =?";

            try(Connection conn = connectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1,category.getUserId());
                ps.setString(2,category.getCatName());
                ps.setInt(3,category.getId());

                ps.executeUpdate();

                return category;
            }catch (SQLException e){
                throw new RuntimeException("Error saving category (updating)", e);
            }
        }
    }

    @Override
    public Category findById(int id) {
        String sql = "SELECT id, user_id, catName, created_at FROM categories WHERE id = ?";

        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Category category  = new Category();
                category.setId(rs.getInt("id"));
                category.setUserId(rs.getInt("user_id"));
                category.setCatName(rs.getString("catName"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                category.setCreatedAt(createdAt);

                return category;
            }else {
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException("Error Retrieving Category By id", e);
        }
    }

    @Override
    public List<Category> findByUserId(int userId) {
        String sql = "SELECT id, user_id, catName, created_at FROM categories WHERE user_id = ?";
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setUserId(rs.getInt("user_id"));
                category.setCatName(rs.getString("catName"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                category.setCreatedAt(createdAt);

                categories.add(category);
            }

            return categories;
        }catch (SQLException e){
            throw new RuntimeException("Failed to retrieve categories by userId", e);
        }
    }

    @Override
    public Category findByUserIdAndName(int userId, String name) {
        String sql = "SELECT id, user_id, catName, created_at FROM categories WHERE user_id = ? AND catName = ?";
        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,userId);
            ps.setString(2,name);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                Category category  = new Category();
                category.setId(rs.getInt("id"));
                category.setUserId(rs.getInt("user_id"));
                category.setCatName(rs.getString("catName"));
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = ts.toLocalDateTime();
                category.setCreatedAt(createdAt);

                return category;
            }else {
                return null;
            }
        }catch (SQLException e){
            throw new RuntimeException("Failed to Retrieve Category by userId and catName", e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";

        try(Connection conn = connectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);

            int rows = ps.executeUpdate();
//            System.out.println(rows+" rows affected");
        }catch (SQLException e){
            throw new RuntimeException("Failed to Delete category by id", e);
        }
    }
}
