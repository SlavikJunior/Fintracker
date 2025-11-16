package com.slavikjunior.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

import com.slavikjunior.util.AppLogger;

@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    private static final Logger log = AppLogger.get(LogoutServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Logout requested");

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("Session invalidated");
        }

        resp.sendRedirect(req.getContextPath() + "/auth");
    }
}