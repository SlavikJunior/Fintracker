package com.slavikjunior.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("🔧 Logout requested");

        // Инвалидируем сессию
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
            System.out.println("✅ Session invalidated");
        }

        resp.sendRedirect(req.getContextPath() + "/auth");
    }
}