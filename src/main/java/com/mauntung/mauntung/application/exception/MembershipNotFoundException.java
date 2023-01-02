package com.mauntung.mauntung.application.exception;

public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException() {
        super("Membership Not Found");
    }
}
