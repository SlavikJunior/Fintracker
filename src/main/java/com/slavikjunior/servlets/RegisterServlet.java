package com.slavikjunior.servlets;

import com.slavikjunior.exceptions.PageForwardException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.slavikjunior.util.UserIsLoggedChecker.isLoggedIn;

@WebServlet(urlPatterns = {"/auth/register", "/auth/register/"})
public class RegisterServlet extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // наверное здесь не надо проверять потому что пользователь может захотеть зарегистрировать новый аккаунт
//        boolean isLoggedIn = isLoggedIn(req);
        try {
            getServletContext().getRequestDispatcher("/html/register.html").forward(req, resp);
        } catch (ServletException e) {
            var pfe = new PageForwardException(e.getMessage());
            LOGGER.log(Level.SEVERE, pfe.getMessage());
            throw pfe;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("Login");
        String password = req.getParameter("Password");
        String email = req.getParameter("Email");

        boolean success = isValidCredentials(login, password, email);

        try {
            if (success) {
                resp.addCookie(new Cookie("auth", "true"));
                resp.sendRedirect(req.getContextPath() + "/main");
                return;
            }
            getServletContext().getRequestDispatcher("/html/invalid_register.html").forward(req, resp);
        } catch (ServletException e) {
            var pfe = new PageForwardException(e.getMessage());
            LOGGER.log(Level.SEVERE, pfe.getMessage());
            throw pfe;
        }
    }

    private boolean isValidCredentials(String login, String password, String email) {
        if (login == null || password == null || email == null) return false;

        login = login.trim();
        password = password.trim();
        email = email.trim();

        if (login.length() < 3 || login.length() > 30) return false;
        if (password.length() < 6 || password.length() > 64) return false;

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailRegex)) return false;

        String loginRegex = "^[a-zA-Z0-9_.-]+$";
        if (!login.matches(loginRegex)) return false;

        // добавление в бд
        return true;
    }
}
