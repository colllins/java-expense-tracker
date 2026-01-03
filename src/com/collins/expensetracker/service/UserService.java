package com.collins.expensetracker.service;

import com.collins.expensetracker.model.User;
import com.collins.expensetracker.repository.UserRepository;

import java.util.List;

/**
 * Application-level operations related to users.
 * Sits between the CLI and the UserRepository.
 */
public class UserService {

    // Data access layer used to persist and load users.
    private final UserRepository userRepository;

    /**
     * Constructs a UserService with the given UserRepository.
     *
     * @param userRepository repository implementation used for user persistence
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user with the provided name and email,
     * saves it through the repository, and returns the persisted instance.
     * <p>
     * The repository is expected to assign an id to the user when saving.
     *
     * @param name  the user's display name
     * @param email the user's email address (assumed unique at the database level)
     * @return the saved User, including its generated id
     */
    public User createUser(String name, String email){
        User user = new User(name, email);
        user  = userRepository.save(user);

        return user;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return list of all persisted users
     */
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /**
     * Looks up a single user by id.
     *
     * @param id database identifier of the user
     * @return the matching user, or null if not found
     */
    public User getUserById(int id){
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email email to search for
     * @return the matching user, or null if no user has this email
     */
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public void deleteUser(int id){
        userRepository.deleteById(id);
    }
}
