package com.mauntung.mauntung.application.exception;

public class MerchantNotFoundException extends RuntimeException {
    public MerchantNotFoundException() {
        super("Merchant Not Found");
    }
}
