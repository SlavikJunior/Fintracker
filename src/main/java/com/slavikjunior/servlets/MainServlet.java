package com.slavikjunior.servlets;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.Transaction;
import com.slavikjunior.util.AppLogger;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(MainServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            log.warning("❌ MainServlet: No user session found");
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        log.info("✅ MainServlet: User ID = " + userId);

        try {
            List<Transaction> transactions = EntityManager.INSTANCE.get(Transaction.class, Map.of("user_id", userId));
            int count = transactions == null ? 0 : transactions.size();
            log.info("📊 Loaded " + count + " transactions");

            request.setAttribute("transactions", transactions);
            request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);

        } catch (Exception e) {
            log.severe("💥 MainServlet: Error loading transactions - " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}