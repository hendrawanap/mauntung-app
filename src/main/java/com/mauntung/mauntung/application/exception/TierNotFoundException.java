package com.mauntung.mauntung.application.exception;

public class TierNotFoundException extends RuntimeException {
    public TierNotFoundException() {
        super("Tier Not Found");
    }
}
