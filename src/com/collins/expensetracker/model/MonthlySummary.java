package com.collins.expensetracker.model;

import java.math.BigDecimal;

/**
 * Holds aggregated income/expense information for a single month.
 */
public class MonthlySummary {
    private int year;                                       // Year of the summary (e.g. 2025).
    private int month;                                      // Month of the summary (1-12).
    private BigDecimal totalIncome;                         // Sum of all INCOME transactions.
    private BigDecimal totalExpense;                        // Sum of all EXPENSE transactions.
    private BigDecimal net;                                 // totalIncome - totalExpense.


    /**
     * Constructs a summary for a given year/month with precomputed totals.
     */
    public MonthlySummary(int year, int month, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal net) {
        this.year = year;
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.net = net;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public BigDecimal getNet() {
        return net;
    }

    @Override
    public String toString() {
        return "MonthlySummary{" +
                "year=" + year +
                ", month=" + month +
                ", totalIncome= $" + totalIncome +
                ", totalExpense= $" + totalExpense +
                ", net= $" + net +
                '}';
    }
}
