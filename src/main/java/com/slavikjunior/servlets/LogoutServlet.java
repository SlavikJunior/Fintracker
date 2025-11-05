package com.slavikjunior.servlets;

import com.slavikjunior.util.AppLogger;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/auth/logout")
public class LogoutServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.info("🔧 Logout requested");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("✅ Session invalidated");
        }
        response.sendRedirect(request.getContextPath() + "/auth");
    }
}