package com.slavikjunior.servlets;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.User;
import com.slavikjunior.services.AuthService;
import com.slavikjunior.util.AppLogger;
import com.slavikjunior.util.PasswordHashUtil;
import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(RegisterServlet.class);
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

        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        log.info("Register attempt: " + login + " (" + email + ")");

        HttpSession session = req.getSession(true);

        if (login == null || password == null || email == null ||
                login.isBlank() || password.isBlank() || email.isBlank()) {
            session.setAttribute("flashError", "Заполните все поля");
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        try {
            if (authService.isLoginExists(login)) {
                session.setAttribute("flashError", "Логин уже занят");
                resp.sendRedirect(req.getContextPath() + "/register");
                return;
            }
            if (authService.isEmailExists(email)) {
                session.setAttribute("flashError", "Email уже занят");
                resp.sendRedirect(req.getContextPath() + "/register");
                return;
            }

            String salt = UUID.randomUUID().toString().substring(0, 32);
            String hashedPassword = PasswordHashUtil.hashPassword(password, salt);

            User user = new User(0, login, hashedPassword, salt, email);
            EntityManager.INSTANCE.create(user);

            log.info("User registered successfully: " + login);

            session.setAttribute(SessionConstants.USER_ID, user.getId());
            session.setAttribute(SessionConstants.USER_LOGIN, user.getLogin());
            session.setAttribute("flashSuccess", "Регистрация успешна! Добро пожаловать!");

            resp.sendRedirect(req.getContextPath() + "/main");

        } catch (Exception e) {
            log.severe("Registration error: " + e.getMessage());
            session.setAttribute("flashError", "Ошибка регистрации");
            resp.sendRedirect(req.getContextPath() + "/register");
        }
    }
}