package com.slavikjunior.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebFilter(urlPatterns = {"/*"})
public class CSRFFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Получаем сессию, но не создаем новую если ее нет
        HttpSession session = httpRequest.getSession(false);

        // Для публичных страниц (регистрация, логин) создаем сессию и токен
        String path = httpRequest.getServletPath();
        boolean isPublicPath = path.startsWith("/auth") || path.equals("/");

        if (session == null && isPublicPath) {
            session = httpRequest.getSession(true);
        }

        // Генерируем CSRF токен для сессии если она существует
        if (session != null && session.getAttribute("csrfToken") == null) {
            session.setAttribute("csrfToken", UUID.randomUUID().toString());
        }

        // Для POST запросов проверяем CSRF токен (кроме публичных endpoints)
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            // Исключаем публичные endpoints из проверки CSRF
            if (path.startsWith("/auth/register") || path.startsWith("/auth/login")) {
                chain.doFilter(request, response);
                return;
            }

            // Для защищенных endpoints проверяем CSRF токен
            if (session != null) {
                String sessionToken = (String) session.getAttribute("csrfToken");
                String requestToken = httpRequest.getParameter("csrfToken");

                if (sessionToken == null || !sessionToken.equals(requestToken)) {
                    System.out.println("❌ CSRF token validation failed");
                    System.out.println("Session token: " + sessionToken);
                    System.out.println("Request token: " + requestToken);
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}