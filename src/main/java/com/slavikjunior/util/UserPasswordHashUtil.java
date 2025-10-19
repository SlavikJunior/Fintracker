package com.slavikjunior.util;

public class UserPasswordHashUtil {

    public static String hashPassword(String password) {
        // todo сделать какое нибудь хитрое хеширование пароля
        return String.valueOf(password.hashCode());
    }
}
