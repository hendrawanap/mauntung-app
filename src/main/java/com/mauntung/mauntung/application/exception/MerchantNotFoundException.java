package com.mauntung.mauntung.application.exception;

import java.util.NoSuchElementException;

public class MerchantNotFoundException extends NoSuchElementException {
    public MerchantNotFoundException() {
        super("Merchant Not Found");
    }
}
