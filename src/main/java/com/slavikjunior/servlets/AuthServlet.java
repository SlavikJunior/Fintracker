package com.slavikjunior.servlets;

import com.slavikjunior.exceptions.PageForwardException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.slavikjunior.util.UserIsLoggedChecker.isLoggedIn;

@WebServlet(urlPatterns = {"/auth", "/auth/"})
public class AuthServlet extends HttpServlet {

    private final static Logger LOGGER = Logger.getLogger(AuthServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html");

        boolean isLoggedIn = isLoggedIn(req);
        if (isLoggedIn) {
            try {
                getServletContext().getRequestDispatcher("/html/main.html").forward(req, resp);
                return;
            } catch (ServletException e) {
                var pfe = new PageForwardException(e.getMessage());
                LOGGER.log(Level.SEVERE, pfe.getMessage());
                throw pfe;
            }
        }

        // иначе показываем страницу входа
        try {
            getServletContext().getRequestDispatcher("/html/auth.html").forward(req, resp);
        } catch (ServletException e) {
            var pfe = new PageForwardException(e.getMessage());
            LOGGER.log(Level.SEVERE, pfe.getMessage());
            throw pfe;
        }
    }
}
