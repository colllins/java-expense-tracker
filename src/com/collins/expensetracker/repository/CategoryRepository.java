package com.collins.expensetracker.repository;

import com.collins.expensetracker.model.Category;

import java.util.List;

/**
 * Repository abstraction for category records.
 * Handles creation, lookup and deletion of categories per user.
 */
public interface CategoryRepository {

    /**
     * Inserts a new category or updates an existing one.
     *
     * @param category category to save
     * @return saved category with generated id if it was new
     */
    Category save(Category category);

    /**
     * Finds a category by its primary key.
     *
     * @param id category id
     * @return matching category or null if not found
     */
    Category findById(int id);

    /**
     * Returns all categories belonging to the given user.
     *
     * @param userId owner user id
     * @return list of that user's categories
     */
    List<Category> findByUserId(int userId);

    /**
     * Finds a category by user and name.
     * Useful to enforce or check uniqueness per user.
     *
     * @param userId owner user id
     * @param name   category name
     * @return matching category or null if none exists
     */
    Category findByUserIdAndName(int userId, String name);

    /**
     * Deletes the category with the given id.
     *
     * @param id category id to delete
     */
    void deleteById(int id);
}
