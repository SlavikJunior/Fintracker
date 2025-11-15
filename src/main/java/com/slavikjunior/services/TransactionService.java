// TransactionService.java
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

    // В методе getAllUserTransactions обновляем:
    public List<TransactionItem> getAllUserTransactions(int userId) {
        List<TransactionItem> allTransactions = new ArrayList<>();

        try {
            // Получаем доходы
            List<IncomeTransaction> incomes = EntityManager.INSTANCE.get(IncomeTransaction.class, Map.of("user_id", userId));
            if (incomes != null) {
                allTransactions.addAll(incomes.stream()
                        .map(income -> {
                            List<String> tagNames = Collections.emptyList();
                            try {
                                tagNames = tagService.getTagNamesForTransaction(income.getId(), "INCOME");
                            } catch (Exception e) {
                                log.warning("⚠️ Error loading tags for income transaction " + income.getId());
                            }
                            return new TransactionItemWrapper(income, "INCOME", tagNames);
                        })
                        .collect(Collectors.toList()));
            }

            // Получаем расходы
            List<ExpenseTransaction> expenses = EntityManager.INSTANCE.get(ExpenseTransaction.class, Map.of("user_id", userId));
            if (expenses != null) {
                allTransactions.addAll(expenses.stream()
                        .map(expense -> {
                            List<String> tagNames = Collections.emptyList();
                            try {
                                tagNames = tagService.getTagNamesForTransaction(expense.getId(), "EXPENSE");
                            } catch (Exception e) {
                                log.warning("⚠️ Error loading tags for expense transaction " + expense.getId());
                            }
                            return new TransactionItemWrapper(expense, "EXPENSE", tagNames);
                        })
                        .collect(Collectors.toList()));
            }

            // Сортируем по дате
            allTransactions.sort((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));

        } catch (Exception e) {
            System.err.println("⚠️ Error loading user transactions: " + e.getMessage());
        }

        return allTransactions;
    }

    public List<TransactionItem> getUserTransactionsWithFilters(int userId, String type, String category,
                                                                LocalDate startDate, LocalDate endDate) {
        List<TransactionItem> filteredTransactions = getAllUserTransactions(userId);

        // Применяем фильтры
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
                    .filter(t -> t.getCreatedAt().after(startTimestamp) ||
                            t.getCreatedAt().equals(startTimestamp))
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

    // TransactionService.java - исправленные методы
    public List<String> getIncomeCategories() {
        try {
            // Теперь передаем null вместо Map.of() - ORM обработает это корректно
            List<IncomeCategory> categories = EntityManager.INSTANCE.get(IncomeCategory.class, null);
            if (categories != null && !categories.isEmpty()) {
                return categories.stream().map(IncomeCategory::getName).collect(Collectors.toList());
            } else {
                // Fallback если таблица категорий пустая
                return Arrays.asList("Зарплата", "Фриланс", "Инвестиции", "Подарки", "Прочее");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading income categories: " + e.getMessage());
            return Arrays.asList("Зарплата", "Фриланс", "Инвестиции", "Подарки", "Прочее");
        }
    }

    public List<String> getExpenseCategories() {
        try {
            // Передаем null - ORM выполнит SELECT * FROM expense_categories
            List<ExpenseCategory> categories = EntityManager.INSTANCE.get(ExpenseCategory.class, null);
            if (categories != null && !categories.isEmpty()) {
                return categories.stream().map(ExpenseCategory::getName).collect(Collectors.toList());
            } else {
                return Arrays.asList("Еда", "Транспорт", "Жилье", "Развлечения", "Здоровье", "Одежда", "Образование", "Другое");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading expense categories: " + e.getMessage());
            return Arrays.asList("Еда", "Транспорт", "Жилье", "Развлечения", "Здоровье", "Одежда", "Образование", "Другое");
        }
    }
}

// Вспомогательный класс для объединения транзакций
class TransactionItemWrapper implements TransactionItem {
    private final Object transaction;
    private final String type;
    private final List<String> tagNames;

    public TransactionItemWrapper(Object transaction, String type, List<String> tagNames) {
        this.transaction = transaction;
        this.type = type;
        this.tagNames = tagNames != null ? tagNames : new ArrayList<>();
    }

    @Override
    public int getId() {
        if (transaction instanceof IncomeTransaction) {
            return ((IncomeTransaction) transaction).getId();
        } else {
            return ((ExpenseTransaction) transaction).getId();
        }
    }

    @Override
    public int getUserId() {
        if (transaction instanceof IncomeTransaction) {
            return ((IncomeTransaction) transaction).getUserId();
        } else {
            return ((ExpenseTransaction) transaction).getUserId();
        }
    }

    @Override
    public BigDecimal getAmount() {
        if (transaction instanceof IncomeTransaction) {
            return ((IncomeTransaction) transaction).getAmount();
        } else {
            return ((ExpenseTransaction) transaction).getAmount();
        }
    }

    @Override
    public String getCategory() {
        if (transaction instanceof IncomeTransaction) {
            return ((IncomeTransaction) transaction).getCategory();
        } else {
            return ((ExpenseTransaction) transaction).getCategory();
        }
    }

    @Override
    public String getDescription() {
        if (transaction instanceof IncomeTransaction) {
            return ((IncomeTransaction) transaction).getDescription();
        } else {
            return ((ExpenseTransaction) transaction).getDescription();
        }
    }

    @Override
    public Timestamp getCreatedAt() {
        if (transaction instanceof IncomeTransaction) {
            return ((IncomeTransaction) transaction).getCreatedAt();
        } else {
            return ((ExpenseTransaction) transaction).getCreatedAt();
        }
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<String> getTagNames() {
        return tagNames;
    }
}