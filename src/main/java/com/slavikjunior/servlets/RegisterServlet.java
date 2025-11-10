package com.slavikjunior.servlets;

import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.models.User;
import com.slavikjunior.services.AuthService;
import com.slavikjunior.util.AppLogger;
import com.slavikjunior.util.PasswordHashUtil;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/auth/register")
public class RegisterServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(RegisterServlet.class);
    private AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String err = req.getParameter("error");
        if ("missing".equals(err)) {
            req.setAttribute("errorMessage", "Заполните все поля");
        } else if ("duplicate".equals(err)) {
            req.setAttribute("errorMessage", "Логин или email уже занят");
        } else if (err != null) {
            req.setAttribute("errorMessage", "Ошибка регистрации");
        }
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        log.info("🧾 RegisterServlet: Attempt to register user " + login + " (" + email + ")");

        if (login == null || password == null || email == null ||
                login.isBlank() || password.isBlank() || email.isBlank()) {
            log.warning("⚠️ Missing registration parameters");
            resp.sendRedirect(req.getContextPath() + "/auth/register?error=missing");
            return;
        }

        try {
            if (authService.isLoginExists(login)) {
                throw new Exception("Login already exists");
            }
            if (authService.isEmailExists(email)) {
                throw new Exception("Email already exists");
            }

            String hashedPassword = PasswordHashUtil.hashPassword(password);
            User user = new User(EntityManager.INSTANCE.getLastId(User.class), login, hashedPassword, email);

            EntityManager.INSTANCE.create(user);
            log.info("✅ User created successfully: " + login);

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("user_login", user.getLogin());
            resp.sendRedirect(req.getContextPath() + "/main");

        } catch (Exception e) {
            log.severe("💥 RegisterServlet: Error registering user - " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/auth/register?error=duplicate");
        }
    }
}