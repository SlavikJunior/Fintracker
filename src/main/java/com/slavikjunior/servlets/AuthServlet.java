package com.slavikjunior.servlets;

import com.slavikjunior.util.AppLogger;
import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static final Logger log = AppLogger.get(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        boolean isLoggedIn = session != null && session.getAttribute(SessionConstants.USER_ID) != null;

        log.info("🔧 AuthServlet: isLoggedIn = " + isLoggedIn);

        if (isLoggedIn) {
            response.sendRedirect(request.getContextPath() + "/main");
        } else {
            request.getRequestDispatcher("/WEB-INF/jsp/auth.jsp").forward(request, response);
        }
    }
}