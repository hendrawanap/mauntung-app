package com.mauntung.mauntung.application.exception;

public class RewardNotFoundException extends RuntimeException {
    public RewardNotFoundException() {
        super("Reward Not Found");
    }
}
