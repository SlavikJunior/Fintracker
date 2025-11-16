package com.slavikjunior.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;
import com.slavikjunior.util.AppLogger;

@WebFilter(urlPatterns = {"/*"})
public class CSRFFilter implements Filter {
    private static final Logger log = AppLogger.get(CSRFFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        String path = httpRequest.getServletPath();
        boolean isPublicPath = path.equals("/") || path.equals("/login") || path.equals("/register");

        if (session == null && isPublicPath) {
            session = httpRequest.getSession(true);
        }

        if (session != null && session.getAttribute("csrfToken") == null) {
            String token = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
            log.info("Generated CSRF token: " + token);
        }

        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            if (path.startsWith("/register") || path.startsWith("/login")) {
                chain.doFilter(request, response);
                return;
            }

            if (session != null) {
                String sessionToken = (String) session.getAttribute("csrfToken");
                String requestToken = httpRequest.getParameter("csrfToken");

                if (sessionToken == null || !sessionToken.equals(requestToken)) {
                    log.warning("CSRF token validation failed. Session: " + sessionToken + ", Request: " + requestToken);
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}