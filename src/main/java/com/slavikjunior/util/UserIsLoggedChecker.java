package com.slavikjunior.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;

import com.slavikjunior.util.AppLogger;

public class UserIsLoggedChecker {
    private static final Logger log = AppLogger.get(UserIsLoggedChecker.class);

    public static boolean isLoggedIn(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            log.info("No session found");
            return false;
        }
        if (session.getAttribute(SessionConstants.USER_ID) != null) {
            log.info("User is logged in (session)");
            return true;
        }
        log.warning("User not logged in (session)");
        return false;
    }

    public static int getUserId(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object userIdObj = session.getAttribute(SessionConstants.USER_ID);
            if (userIdObj instanceof Integer userId) {
                return userId;
            }
        }
        log.warning("No userId in session");
        return -1;
    }

    public static String getUserLogin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(SessionConstants.USER_LOGIN);
        }
        return null;
    }
}