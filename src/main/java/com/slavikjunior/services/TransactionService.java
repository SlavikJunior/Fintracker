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
            if ("INCOME".equals(type)) {
                IncomeTransaction transaction = new IncomeTransaction(0, userId, amount, category, description);
                EntityManager.INSTANCE.create(transaction);
            } else {
                ExpenseTransaction transaction = new ExpenseTransaction(0, userId, amount, category, description);
                EntityManager.INSTANCE.create(transaction);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TransactionItem> getAllUserTransactions(int userId) {
        List<TransactionItem> allTransactions = new ArrayList<>();

        try {
            // Доходы
            List<IncomeTransaction> incomes = EntityManager.INSTANCE.get(IncomeTransaction.class, Map.of("user_id", userId));
            if (incomes != null) {
                for (IncomeTransaction income : incomes) {
                    List<Tag> tags = tagService.getTagsForTransaction(income.getId(), "INCOME");
                    allTransactions.add(new TransactionWithTags(
                            income.getId(), income.getUserId(), income.getAmount(),
                            income.getCategory(), income.getDescription(), income.getCreatedAt(),
                            "INCOME", tags
                    ));
                }
            }

            // Расходы
            List<ExpenseTransaction> expenses = EntityManager.INSTANCE.get(ExpenseTransaction.class, Map.of("user_id", userId));
            if (expenses != null) {
                for (ExpenseTransaction expense : expenses) {
                    List<Tag> tags = tagService.getTagsForTransaction(expense.getId(), "EXPENSE");
                    allTransactions.add(new TransactionWithTags(
                            expense.getId(), expense.getUserId(), expense.getAmount(),
                            expense.getCategory(), expense.getDescription(), expense.getCreatedAt(),
                            "EXPENSE", tags
                    ));
                }
            }

            // Сортировка по дате (новые сверху)
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
                    .filter(t -> t.getCreatedAt().after(startTimestamp) || t.getCreatedAt().equals(startTimestamp))
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

    public boolean deleteTransaction(String type, int transactionId) {
        try {
            if ("INCOME".equals(type)) {
                return EntityManager.INSTANCE.delete(IncomeTransaction.class, transactionId);
            } else {
                return EntityManager.INSTANCE.delete(ExpenseTransaction.class, transactionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getIncomeCategories() {
        try {
            List<IncomeCategory> categories = EntityManager.INSTANCE.get(IncomeCategory.class, null);
            if (categories != null && !categories.isEmpty()) {
                return categories.stream().map(IncomeCategory::getName).collect(Collectors.toList());
            } else {
                return Arrays.asList("Зарплата", "Фриланс", "Инвестиции", "Подарки", "Прочее");
            }
        } catch (Exception e) {
            System.err.println("Error loading income categories: " + e.getMessage());
            return Arrays.asList("Зарплата", "Фриланс", "Инвестиции", "Подарки", "Прочее");
        }
    }

    public List<String> getExpenseCategories() {
        try {
            List<ExpenseCategory> categories = EntityManager.INSTANCE.get(ExpenseCategory.class, null);
            if (categories != null && !categories.isEmpty()) {
                return categories.stream().map(ExpenseCategory::getName).collect(Collectors.toList());
            } else {
                return Arrays.asList("Еда", "Транспорт", "Жилье", "Развлечения", "Здоровье", "Одежда", "Образование", "Другое");
            }
        } catch (Exception e) {
            System.err.println("Error loading expense categories: " + e.getMessage());
            return Arrays.asList("Еда", "Транспорт", "Жилье", "Развлечения", "Здоровье", "Одежда", "Образование", "Другое");
        }
    }
}