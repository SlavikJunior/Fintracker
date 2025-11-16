package com.slavikjunior.services;

import com.slavikjunior.models.User;
import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.util.PasswordHashUtil;
import com.slavikjunior.util.AppLogger;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AuthService {
    private static final Logger log = AppLogger.get(AuthService.class);

    public User authenticate(String login, String password) {
        try {
            log.info("Auth attempt for: " + login);
            User user = EntityManager.INSTANCE.getUnique(User.class, Map.of("login", login));
            if (user != null) {
                log.info("User found: " + user.getLogin() + ", ID: " + user.getId());

                String hashedPassword = PasswordHashUtil.hashPassword(password, user.getSalt());
                if (user.getPassword().equals(hashedPassword)) {
                    log.info("Password correct");
                    return user;
                } else {
                    log.warning("Password incorrect");
                }
            } else {
                log.warning("User not found: " + login);
            }
            return null;
        } catch (Exception e) {
            log.severe("Auth exception: " + e.getMessage());
            return null;
        }
    }

    public boolean isLoginExists(String login) {
        try {
            List<User> users = EntityManager.INSTANCE.get(User.class, Map.of("login", login));
            return !users.isEmpty();
        } catch (Exception e) {
            log.severe("Error checking login: " + e.getMessage());
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        try {
            List<User> users = EntityManager.INSTANCE.get(User.class, Map.of("email", email));
            return !users.isEmpty();
        } catch (Exception e) {
            log.severe("Error checking email: " + e.getMessage());
            return false;
        }
    }
}