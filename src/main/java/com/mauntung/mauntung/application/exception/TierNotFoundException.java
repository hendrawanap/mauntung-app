package com.mauntung.mauntung.application.exception;

import java.util.NoSuchElementException;

public class TierNotFoundException extends NoSuchElementException {
    public TierNotFoundException() {
        super("Tier Not Found");
    }
}
