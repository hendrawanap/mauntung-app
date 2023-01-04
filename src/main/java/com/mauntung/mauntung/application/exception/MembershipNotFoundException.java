package com.mauntung.mauntung.application.exception;

import java.util.NoSuchElementException;

public class MembershipNotFoundException extends NoSuchElementException {
    public MembershipNotFoundException() {
        super("Membership Not Found");
    }
}
