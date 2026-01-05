package com.collins.expensetracker.service;

import com.collins.expensetracker.model.Category;
import com.collins.expensetracker.repository.CategoryRepository;

import java.util.List;

/**
 * Service layer for category-related operations.
 * Wraps the CategoryRepository and will contain any
 * business logic around categories.
 */
public class CategoryService {

    /**
     * Underlying repository used to access category data in the database.
     */
    private final CategoryRepository categoryRepository;

    /**
     * Creates a CategoryService that uses the given repository.
     *
     * @param categoryRepository repository implementation for categories
     */
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new category for the given user and saves it in the database.
     *
     * @param userId the owner user id
     * @param name   the category name (for example "Groceries", "Rent")
     * @return the saved Category including its generated id
     */
    public Category createCategory(int userId, String name){
        Category category = new Category(userId, name);
        category = categoryRepository.save(category);

        return category;
    }

    /**
     * Returns all categories that belong to the given user.
     *
     * @param userId the id of the user whose categories should be loaded
     * @return list of Category records owned by that user (may be empty)
     */
    public List<Category> getCategoriesForUserId(int userId){
        return categoryRepository.findByUserId(userId);
    }

    /**
     * Looks up a single category by its id.
     *
     * @param id the category id
     * @return the Category if found, or null if no category has that id
     */
    public Category getCategoryById(int id){
        return categoryRepository.findById(id);
    }

//    public Category getCategoryByUserName(int userId, String name){
//        return categoryRepository.findByUserIdAndName(userId, name);
//    }

    public void deleteCategory(int id){
        categoryRepository.deleteById(id);
    }
}
