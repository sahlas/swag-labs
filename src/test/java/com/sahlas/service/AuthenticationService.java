package com.sahlas.service;

import com.sahlas.domain.User;
import com.sahlas.domain.UserTable;

public class AuthenticationService {

    public static boolean authenticate(String userName, String password) {
        User user = UserTable.getUser(userName);
        if (user == null) {
            return false; // User not found
        }
        return user.password().equals(password); // Validate password
    }
}