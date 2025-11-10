package com.slavikjunior.servlets;

import com.slavikjunior.models.User;
import com.slavikjunior.services.AuthService;
import com.slavikjunior.util.AppLogger;

import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(LoginServlet.class);
    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getParameter("error") != null) {
            req.setAttribute("errorMessage", "Неверный логин или пароль");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        log.info("🔧 LoginServlet: Auth attempt for " + login);

        try {
            User user = authService.authenticate(login, password);
            if (user == null) {
                log.warning("❌ Invalid credentials for: " + login);
                resp.sendRedirect(req.getContextPath() + "/login?error=true");
                return;
            }

            log.info("✅ User authenticated successfully, ID: " + user.getId());
            HttpSession session = req.getSession(true);
            session.setAttribute(SessionConstants.USER_ID, user.getId());
            session.setAttribute(SessionConstants.USER_LOGIN, user.getLogin());
            resp.sendRedirect(req.getContextPath() + "/main");
            log.info("redirect to " + req.getContextPath() + "/main");
        } catch (Exception e) {
            log.severe("💥 LoginServlet: Error during authentication - " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}