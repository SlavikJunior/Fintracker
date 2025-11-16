package com.slavikjunior.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class TransactionGroup {
    private final Date date;
    private final List<TransactionItem> transactions;
    private BigDecimal dayIncome;
    private BigDecimal dayExpense;
    private BigDecimal dayBalance;

    public TransactionGroup(Date date, List<TransactionItem> transactions) {
        this.date = date;
        this.transactions = transactions;
        calculateTotals();
    }

    private void calculateTotals() {
        this.dayIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .map(TransactionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.dayExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .map(TransactionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.dayBalance = dayIncome.subtract(dayExpense);
    }

    public Date getDate() { return date; }
    public List<TransactionItem> getTransactions() { return transactions; }
    public BigDecimal getDayIncome() { return dayIncome; }
    public BigDecimal getDayExpense() { return dayExpense; }
    public BigDecimal getDayBalance() { return dayBalance; }

    @Override
    public String toString() {
        return "TransactionGroup{date=" + date + ", size=" + transactions.size() + "}";
    }
}