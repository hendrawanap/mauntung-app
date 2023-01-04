package com.mauntung.mauntung.application.exception;

import java.util.NoSuchElementException;

public class RewardNotFoundException extends NoSuchElementException {
    public RewardNotFoundException() {
        super("Reward Not Found");
    }
}
