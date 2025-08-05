package com.sahlas.domain;

public record User(String userName, String password) {

    // Factory method for creating users
    public static User of(String userName, String password) {
        return new User(userName, password);
    }
}