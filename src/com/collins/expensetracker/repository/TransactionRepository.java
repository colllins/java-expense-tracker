package com.collins.expensetracker.repository;

import com.collins.expensetracker.model.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository abstraction for transaction records.
 * Encapsulates JDBC access to the transactions table.
 */
public interface TransactionRepository {

    /**
     * Inserts a new transaction or updates an existing one.
     *
     * @param tx transaction to save
     * @return saved transaction with generated id if it was new
     */
    Transaction save(Transaction tx);

    /**
     * Finds a transaction by its primary key.
     *
     * @param id transaction id
     * @return matching transaction or null if not found
     */
    Transaction findById(int id);

    /**
     * Returns all transactions for a given user.
     *
     * @param userId owner user id
     * @return list of that user's transactions
     */
    List<Transaction> findByUserId(int userId);

    /**
     * Returns all transactions for a user in the given date range (inclusive).
     *
     * @param userId owner user id
     * @param from   start date (inclusive)
     * @param to     end date (inclusive)
     * @return list of matching transactions
     */
    List<Transaction> findByUserIdAndDateRange(int userId, LocalDate from, LocalDate to);

    /**
     * Deletes the transaction with the given id.
     *
     * @param id transaction id to delete
     */
    void deleteById(int id);
}
