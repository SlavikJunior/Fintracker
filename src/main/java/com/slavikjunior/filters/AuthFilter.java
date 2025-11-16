package com.slavikjunior.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;
import com.slavikjunior.util.SessionConstants;
import com.slavikjunior.util.AppLogger;

@WebFilter(urlPatterns = {"/main", "/main/", "/transactions", "/transactions/*"})
public class AuthFilter implements Filter {
    private static final Logger log = AppLogger.get(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.USER_ID) == null) {
            log.info("Unauthorized access attempt to " + httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth");
            return;
        }

        chain.doFilter(request, response);
    }
}