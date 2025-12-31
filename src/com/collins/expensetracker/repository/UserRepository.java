package com.collins.expensetracker.repository;

import com.collins.expensetracker.model.User;

import java.util.List;


/**
 * Repository abstraction for working with users in the database.
 * Implementations hide the JDBC details from the rest of the app.
 */
public interface UserRepository {

    /**
     * Inserts a new user or updates an existing one.
     *
     * @param user user to save
     * @return saved user with generated id if it was a new record
     */
    User save(User user);

    /**
     * Finds a user by its id.
     *
     * @param id database primary key
     * @return matching user or null if not found
     */
    User findById(int id);

    /**
     * Finds a user by email address.
     *
     * @param email user email
     * @return matching user or null if not found
     */
    User findByEmail(String email);

    /**
     * Returns all users in the database.
     *
     * @return list of all users
     */
    List<User> findAll();

    /**
     * Deletes a user with the given id, if it exists.
     *
     * @param id id of the user to delete
     */
    void deleteById(int id);
}
