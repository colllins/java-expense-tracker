package com.collins.expensetracker.service;

import com.collins.expensetracker.model.MonthlySummary;
import com.collins.expensetracker.model.Transaction;
import com.collins.expensetracker.model.TransactionType;
import com.collins.expensetracker.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer for transaction-related operations.
 * Orchestrates creation and retrieval of Transaction records
 * using the underlying TransactionRepository.
 */
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * Constructs a TransactionService with the given repository.
     *
     * @param transactionRepository repository used to persist and read transactions
     */
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates and saves a new transaction (income or expense) for a user.
     *
     * @param userId      owner of the transaction
     * @param catId       category this transaction belongs to
     * @param type        transaction type (INCOME or EXPENSE)
     * @param amount      monetary amount of the transaction
     * @param date        date the transaction occurred
     * @param description text description of the transaction
     * @return the saved Transaction with its generated id
     */
    public Transaction addTransaction(int userId, int catId, TransactionType type, BigDecimal amount, LocalDate date,
                                      String description){
        Transaction transaction = new Transaction(userId,catId,type,amount,date,description);
        transaction = transactionRepository.save(transaction);

        return transaction;
    }

    /**
     * Returns all transactions for the given user.
     *
     * @param userId id of the user whose transactions should be fetched
     * @return list of transactions belonging to that user
     */
    public List<Transaction> getTransactionForUser(int userId){
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Returns all transactions for a user within a given date range (inclusive).
     *
     * @param userId id of the user whose transactions are requested
     * @param from   start date (inclusive)
     * @param to     end date (inclusive)
     * @return list of transactions for that user between from and to
     */
    public List<Transaction> getTransactionsForUserInRange(int userId, LocalDate from, LocalDate to){
        return transactionRepository.findByUserIdAndDateRange(userId,from,to);
    }

    /**
     * Computes a monthly summary (income, expenses, net) for a given user and month.
     *
     * @param userId the user whose transactions to summarize
     * @param year   the year of the month to summarize
     * @param month  the month (1–12) to summarize
     * @return a MonthlySummary containing total income, total expenses, and net
     */
    MonthlySummary getMonthlySummary(int userId, int year, int month){

        // First and last day of the requested month
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // All transactions for this user in that month
        List<Transaction> transactions = getTransactionsForUserInRange(userId,start,end);

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        // Sum income and expenses separately
        for (Transaction tx:transactions) {
            if(tx.getType() == TransactionType.INCOME){
                income = income.add(tx.getAmount());
            }else if(tx.getType() == TransactionType.EXPENSE){
                expense = expense.add(tx.getAmount());
            }
        }

        // Net = income - expenses
        BigDecimal net = income.subtract(expense);

        return new MonthlySummary(year,month,income,expense,net);
    }

    /**
     * Deletes a transaction with the given id.
     * <p>
     * This method delegates to the repository to remove the transaction
     * from persistent storage. If the id does not exist, the repository
     * may simply do nothing.
     *
     * @param id the identifier of the transaction to delete
     */
    public void deleteTransaction(int id){
        transactionRepository.deleteById(id);
    }
}