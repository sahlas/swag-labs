package com.sahlas.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserTable {

    private static final String DEFAULT_PASSWORD = "secret_sauce";

    private static final List<User> USERS = List.of(
            new User("standard_user", DEFAULT_PASSWORD),
            new User("locked_out_user", DEFAULT_PASSWORD),
            new User("problem_user", DEFAULT_PASSWORD),
            new User("performance_glitch_user", DEFAULT_PASSWORD),
            new User("error_user", DEFAULT_PASSWORD),
            new User("visual_user", DEFAULT_PASSWORD)
    );

    private static final Map<String, User> USER_MAP = USERS.stream()
            .collect(Collectors.toMap(User::userName, Function.identity()));

    public static List<User> getAllUsers() {
        return USERS;
    }

    public static User getUser(String userName) {
        return USER_MAP.get(userName);
    }

    public static boolean isValidUser(String userName) {
        return USER_MAP.containsKey(userName);
    }
}