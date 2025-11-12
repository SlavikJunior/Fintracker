// MainServlet.java
package com.slavikjunior.servlets;

import com.slavikjunior.models.TransactionGroup;
import com.slavikjunior.models.TransactionItem;
import com.slavikjunior.services.TransactionService;
import com.slavikjunior.util.AppLogger;
import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(MainServlet.class);
    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.info("GET /main");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.USER_ID) == null) {
            log.warning("❌ MainServlet: No user session found");
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        int userId = (int) session.getAttribute(SessionConstants.USER_ID);
        log.info("✅ MainServlet: User ID = " + userId);

        try {
            // Получаем параметры фильтрации
            String filterType = request.getParameter("type");
            String filterCategory = request.getParameter("category");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");

            LocalDate startDate = null;
            LocalDate endDate = null;

            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }

            // Получаем транзакции с учетом фильтров
            List<TransactionItem> transactions = transactionService.getUserTransactionsWithFilters(
                    userId, filterType, filterCategory, startDate, endDate
            );

            int count = transactions.size();
            log.info("📊 Loaded " + count + " transactions with filters");

            // Группируем транзакции по дням
            Map<java.sql.Date, List<TransactionItem>> groupedTransactions = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> new java.sql.Date(t.getCreatedAt().getTime())
                    ));

            // Сортируем по дате (новые сверху)
            List<TransactionGroup> transactionGroups = groupedTransactions.entrySet().stream()
                    .sorted(Map.Entry.<java.sql.Date, List<TransactionItem>>comparingByKey().reversed())
                    .map(entry -> new TransactionGroup(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            // Считаем общие итоги
            BigDecimal totalIncome = transactions.stream()
                    .filter(t -> "INCOME".equals(t.getType()))
                    .map(TransactionItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpense = transactions.stream()
                    .filter(t -> "EXPENSE".equals(t.getType()))
                    .map(TransactionItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalBalance = totalIncome.subtract(totalExpense);

            // Получаем категории для фильтров
            List<String> incomeCategories = transactionService.getIncomeCategories();
            List<String> expenseCategories = transactionService.getExpenseCategories();

            // Объединяем все категории для фильтра
            List<String> allCategories = new ArrayList<>();
            allCategories.addAll(incomeCategories);
            allCategories.addAll(expenseCategories);

            request.setAttribute("transactionGroups", transactionGroups);
            request.setAttribute("totalIncome", totalIncome);
            request.setAttribute("totalExpense", totalExpense);
            request.setAttribute("totalBalance", totalBalance);
            request.setAttribute("incomeCategories", incomeCategories);
            request.setAttribute("expenseCategories", expenseCategories);
            request.setAttribute("allCategories", allCategories);

            // Сохраняем параметры фильтров для отображения в форме
            request.setAttribute("filterType", filterType);
            request.setAttribute("filterCategory", filterCategory);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);

            request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);

        } catch (Exception e) {
            log.severe("💥 MainServlet: Error loading transactions - " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}