package com.mauntung.mauntung.application.exception;

public class UserWithSpecifiedEmailExistedException extends IllegalArgumentException {
    public UserWithSpecifiedEmailExistedException() {
        super("User with specified email is already existed");
    }
}
