package com.slavikjunior.services;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.*;
import com.slavikjunior.util.AppLogger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionService {

    private static final Logger log = AppLogger.get(TransactionService.class);
    private TagService tagService = new TagService();

    public boolean createTransaction(int userId, BigDecimal amount, String category, String description, String type) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.warning("Invalid amount: " + amount);
                return false;
            }
            Transaction transaction = new Transaction(0, userId, amount, category, description, type.toUpperCase());
            EntityManager.INSTANCE.create(transaction);
            return true;
        } catch (Exception e) {
            log.severe("Error creating transaction: " + e.getMessage());
            return false;
        }
    }

    public List<TransactionItem> getAllUserTransactions(int userId) {
        List<TransactionItem> allTransactions = new ArrayList<>();

        try {
            List<Transaction> transactions = EntityManager.INSTANCE.get(Transaction.class, Map.of("user_id", userId));

            for (Transaction transaction : transactions) {
                List<Tag> tags = tagService.getTagsForTransaction(transaction.getId());
                allTransactions.add(new TransactionWithTags(
                        transaction.getId(), transaction.getUserId(), transaction.getAmount(),
                        transaction.getCategory(), transaction.getDescription(), transaction.getCreatedAt(),
                        transaction.getType(), tags
                ));
            }

            allTransactions.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));

        } catch (Exception e) {
            log.severe("Error loading user transactions: " + e.getMessage());
        }

        return allTransactions;
    }

    public List<TransactionItem> getUserTransactionsWithFilters(int userId, String type, String category,
                                                                LocalDate startDate, LocalDate endDate) {
        List<TransactionItem> filteredTransactions = getAllUserTransactions(userId);

        if (type != null && !type.isEmpty()) {
            filteredTransactions = filteredTransactions.stream()
                    .filter(t -> type.equals(t.getType()))
                    .collect(Collectors.toList());
        }

        if (category != null && !category.isEmpty()) {
            filteredTransactions = filteredTransactions.stream()
                    .filter(t -> category.equals(t.getCategory()))
                    .collect(Collectors.toList());
        }

        if (startDate != null) {
            Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
            filteredTransactions = filteredTransactions.stream()
                    .filter(t -> !t.getCreatedAt().before(startTimestamp))
                    .collect(Collectors.toList());
        }

        if (endDate != null) {
            Timestamp endTimestamp = Timestamp.valueOf(endDate.plusDays(1).atStartOfDay());
            filteredTransactions = filteredTransactions.stream()
                    .filter(t -> t.getCreatedAt().before(endTimestamp))
                    .collect(Collectors.toList());
        }

        return filteredTransactions;
    }

    public boolean deleteTransaction(int transactionId) {
        try {
            return EntityManager.INSTANCE.delete(Transaction.class, transactionId);
        } catch (Exception e) {
            log.severe("Error deleting transaction: " + e.getMessage());
            return false;
        }
    }

    public List<String> getCategories(String type) {
        try {
            List<Category> categories = EntityManager.INSTANCE.get(Category.class, Map.of("type", type.toUpperCase()));
            return categories.stream().map(Category::getName).collect(Collectors.toList());
        } catch (Exception e) {
            log.severe("Error loading categories: " + e.getMessage());
            return type.equals("INCOME") ? Arrays.asList("Зарплата", "Фриланс", "Инвестиции", "Подарки", "Прочее")
                    : Arrays.asList("Еда", "Транспорт", "Жилье", "Развлечения", "Здоровье", "Одежда", "Образование", "Другое");
        }
    }
}