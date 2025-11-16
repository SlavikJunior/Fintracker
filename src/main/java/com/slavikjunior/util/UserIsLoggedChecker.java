package com.slavikjunior.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.logging.Logger;

public class UserIsLoggedChecker {

    private static final Logger log = AppLogger.get(UserIsLoggedChecker.class);

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
}