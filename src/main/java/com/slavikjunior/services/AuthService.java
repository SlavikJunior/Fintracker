package com.slavikjunior.services;

import com.slavikjunior.models.User;
import com.slavikjunior.deorm.orm.EntityManager;
import com.slavikjunior.util.PasswordHashUtil;

import java.util.List;
import java.util.Map;

public class AuthService {

    public User authenticate(String login, String password) {
        try {
            System.out.println("🔧 Auth attempt for: " + login);
            List<User> users = EntityManager.INSTANCE.get(User.class, Map.of("login", login));
            if (users != null && !users.isEmpty()) {
                User user = users.get(0);
                System.out.println("✅ User found: " + user.getLogin() + ", ID: " + user.getId());

                String hashedPassword = PasswordHashUtil.hashPassword(password);
                if (user.getPassword().equals(hashedPassword)) {
                    System.out.println("✅ Password correct");
                    return user;
                } else {
                    System.out.println("❌ Password incorrect");
                }
            } else {
                System.out.println("❌ User not found: " + login);
            }
            return null;
        } catch (Exception e) {
            System.err.println("💥 Auth exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean isLoginExists(String login) {
        try {
            List<User> users = EntityManager.INSTANCE.get(User.class, Map.of("login", login));
            return users != null && !users.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        try {
            List<User> users = EntityManager.INSTANCE.get(User.class, Map.of("email", email));
            return users != null && !users.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}