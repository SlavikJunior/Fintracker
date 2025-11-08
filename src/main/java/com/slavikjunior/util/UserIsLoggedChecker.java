package com.slavikjunior.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class UserIsLoggedChecker {

    public static boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("🔍 No session found");
            return false;
        }
        if (session.getAttribute("user_id") != null) {
            System.out.println("✅ User is logged in (session)");
            return true;
        }
        System.out.println("❌ User not logged in (session)");
        return false;
    }

    public static int getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object userIdObj = session.getAttribute("user_id");
            if (userIdObj instanceof Integer userId) {
                return userId;
            }
        }
        System.out.println("❌ No userId in session");
        return -1;
    }

    public static String getUserLogin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return (String) session.getAttribute("user_login");
        }
        return null;
    }
}