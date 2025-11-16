package com.slavikjunior.servlets;

import com.slavikjunior.services.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import static com.slavikjunior.util.UserIsLoggedChecker.getUserId;

@WebServlet(urlPatterns = {"/transactions", "/transactions/delete"})
public class TransactionServlet extends HttpServlet {

    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String path = req.getServletPath();

        if ("/transactions".equals(path)) {
            addTransaction(req, resp, session);
        } else if ("/transactions/delete".equals(path)) {
            deleteTransaction(req, resp, session);
        }
    }

    private void addTransaction(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        int userId = getUserId(req);
        String amountStr = req.getParameter("amount");
        String category = req.getParameter("category");
        String description = req.getParameter("description");
        String type = req.getParameter("type");

        if (amountStr == null || amountStr.isEmpty() || category == null || category.isEmpty() || type == null) {
            session.setAttribute("flashError", "Заполните все обязательные поля");
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                session.setAttribute("flashError", "Сумма должна быть положительной");
                resp.sendRedirect(req.getContextPath() + "/main");
                return;
            }

            boolean success = transactionService.createTransaction(userId, amount, category, description, type);
            if (success) {
                session.setAttribute("flashSuccess", "Транзакция успешно добавлена");
            } else {
                session.setAttribute("flashError", "Ошибка базы данных");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Неверный формат суммы");
        }

        resp.sendRedirect(req.getContextPath() + "/main");
    }

    private void deleteTransaction(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        String transactionIdStr = req.getParameter("transactionId");

        if (transactionIdStr == null || transactionIdStr.isEmpty()) {
            session.setAttribute("flashError", "Неверный ID транзакции");
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }

        try {
            int transactionId = Integer.parseInt(transactionIdStr);
            boolean success = transactionService.deleteTransaction(transactionId);

            if (success) {
                session.setAttribute("flashSuccess", "Транзакция удалена");
            } else {
                session.setAttribute("flashError", "Ошибка при удалении");
            }
        } catch (NumberFormatException e) {
            session.setAttribute("flashError", "Неверный ID транзакции");
        }

        resp.sendRedirect(req.getContextPath() + "/main");
    }
}