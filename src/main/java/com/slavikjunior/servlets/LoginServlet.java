package com.slavikjunior.servlets;

import com.slavikjunior.models.User;
import com.slavikjunior.services.AuthService;
import com.slavikjunior.util.AppLogger;
import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(LoginServlet.class);
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // === FLASH MESSAGES ===
        HttpSession session = req.getSession(false);
        if (session != null) {
            String flashError = (String) session.getAttribute("flashError");
            if (flashError != null) {
                req.setAttribute("errorMessage", flashError);
                session.removeAttribute("flashError");
            }
        }
        // ======================

        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        log.info("Login attempt for: " + login);

        User user = authService.authenticate(login, password);

        HttpSession session = req.getSession(true);

        if (user == null) {
            session.setAttribute("flashError", "Неверный логин или пароль");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        log.info("User authenticated: " + user.getId());
        session.setAttribute(SessionConstants.USER_ID, user.getId());
        session.setAttribute(SessionConstants.USER_LOGIN, user.getLogin());

        session.setAttribute("flashSuccess", "Добро пожаловать!");
        resp.sendRedirect(req.getContextPath() + "/main");
    }
}