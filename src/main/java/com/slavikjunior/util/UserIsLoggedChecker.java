package com.slavikjunior.util;

import jakarta.servlet.http.HttpServletRequest;

public class UserIsLoggedChecker {

    public static boolean isLoggedIn(HttpServletRequest req) {
        var cookies = req.getCookies();
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals("auth") && "true".equals(cookie.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
