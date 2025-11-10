package com.slavikjunior.servlets;

import com.slavikjunior.services.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

import static com.slavikjunior.util.UserIsLoggedChecker.getUserId;

@WebServlet(urlPatterns = {"/transactions", "/transactions/delete"})
public class TransactionServlet extends HttpServlet {

    private TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/transactions".equals(path)) {
            addTransaction(req, resp);
        } else if ("/transactions/delete".equals(path)) {
            deleteTransaction(req, resp);
        }
    }

    // TransactionServlet.java - в методе addTransaction
    private void addTransaction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = getUserId(req);
        String amountStr = req.getParameter("amount");
        String category = req.getParameter("category");
        String description = req.getParameter("description");
        String type = req.getParameter("type"); // Новый параметр

        if (amountStr == null || amountStr.isEmpty() || category == null || category.isEmpty() || type == null) {
            resp.sendRedirect(req.getContextPath() + "/main?error=invalid_data");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            boolean success = transactionService.createTransaction(userId, amount, category, description, type);

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/main?success=true");
            } else {
                resp.sendRedirect(req.getContextPath() + "/main?error=db_error");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/main?error=invalid_amount");
        }
    }

    private void deleteTransaction(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String transactionIdStr = req.getParameter("transactionId");

        if (transactionIdStr == null || transactionIdStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/main?error=invalid_id");
            return;
        }

        try {
            int transactionId = Integer.parseInt(transactionIdStr);
            boolean success = transactionService.deleteTransaction(transactionId);

            if (success) {
                resp.sendRedirect(req.getContextPath() + "/main?success=deleted");
            } else {
                resp.sendRedirect(req.getContextPath() + "/main?error=delete_failed");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/main?error=invalid_id");
        }
    }
}