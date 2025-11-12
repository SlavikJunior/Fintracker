package com.slavikjunior.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionGroup {
    private Date date;
    private List<TransactionItem> transactions;
    private BigDecimal dayIncome;
    private BigDecimal dayExpense;
    private BigDecimal dayBalance;

    public TransactionGroup(Date date, List<TransactionItem> transactions) {
        this.date = date;
        this.transactions = transactions;
        calculateTotals();
    }

    private void calculateTotals() {
        this.dayIncome = BigDecimal.ZERO;
        this.dayExpense = BigDecimal.ZERO;

        for (TransactionItem transaction : transactions) {
            if ("INCOME".equals(transaction.getType())) {
                dayIncome = dayIncome.add(transaction.getAmount());
            } else {
                dayExpense = dayExpense.add(transaction.getAmount());
            }
        }

        this.dayBalance = dayIncome.subtract(dayExpense);
    }

    public Date getDate() { return date; }
    public List<TransactionItem> getTransactions() { return transactions; }
    public BigDecimal getDayIncome() { return dayIncome; }
    public BigDecimal getDayExpense() { return dayExpense; }
    public BigDecimal getDayBalance() { return dayBalance; }
}