package com.slavikjunior.filters;

import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/main", "/main/", "/transactions", "/transactions/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.USER_ID) == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth");
            return;
        }

        chain.doFilter(request, response);
    }
}