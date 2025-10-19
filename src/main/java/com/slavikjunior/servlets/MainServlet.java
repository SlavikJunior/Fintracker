package com.slavikjunior.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.slavikjunior.util.UserIsLoggedChecker.isLoggedIn;

@WebServlet(urlPatterns = {"/main", "/main/"})
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean isLoggedIn = isLoggedIn(req);
        if (isLoggedIn)
            getServletContext().getRequestDispatcher("/html/main.html").forward(req, resp);
        else
            resp.sendRedirect(req.getContextPath() + "/auth/login");
    }
}
