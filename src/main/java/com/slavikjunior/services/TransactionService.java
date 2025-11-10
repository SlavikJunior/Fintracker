package com.slavikjunior.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.Transaction;

public class TransactionService {

    public boolean createTransaction(int userId, BigDecimal amount, String category, String description) {
        try {
            Transaction transaction = new Transaction(0, userId, amount, category, description);
            EntityManager.INSTANCE.create(transaction);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Transaction> getUserTransactions(int userId) {
        try {
            // Ищем транзакции по user_id в таблице transactions
            return EntityManager.INSTANCE.get(Transaction.class, Map.of("user_id", userId));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean deleteTransaction(int transactionId) {
        try {
            return EntityManager.INSTANCE.delete(Transaction.class, transactionId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}