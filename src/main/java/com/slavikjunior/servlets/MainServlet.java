package com.slavikjunior.servlets;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.Transaction;
import com.slavikjunior.models.TransactionGroup;
import com.slavikjunior.util.AppLogger;

import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(MainServlet.class);

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

        // MainServlet.java - в методе doGet после получения transactions
        try {
            List<Transaction> transactions = EntityManager.INSTANCE.get(Transaction.class, Map.of("user_id", userId));
            int count = transactions == null ? 0 : transactions.size();
            log.info("📊 Loaded " + count + " transactions");

            // Группируем транзакции по дням
            Map<java.sql.Date, List<Transaction>> groupedTransactions = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> new java.sql.Date(t.getCreatedAt().getTime())
                    ));

            // Сортируем по дате (новые сверху)
            List<TransactionGroup> transactionGroups = groupedTransactions.entrySet().stream()
                    .sorted(Map.Entry.<java.sql.Date, List<Transaction>>comparingByKey().reversed())
                    .map(entry -> new TransactionGroup(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            // Считаем общие итоги
            BigDecimal totalIncome = transactions.stream()
                    .filter(t -> "INCOME".equals(t.getType()))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpense = transactions.stream()
                    .filter(t -> "EXPENSE".equals(t.getType()))
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalBalance = totalIncome.subtract(totalExpense);

            request.setAttribute("transactionGroups", transactionGroups);
            request.setAttribute("totalIncome", totalIncome);
            request.setAttribute("totalExpense", totalExpense);
            request.setAttribute("totalBalance", totalBalance);
            request.setAttribute("transactions", transactions); // оставляем для обратной совместимости

            request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);

        } catch (Exception e) {
            log.severe("💥 MainServlet: Error loading transactions - " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}