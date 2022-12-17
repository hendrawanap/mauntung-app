package com.mauntung.mauntung.application.port.customer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class CustomerRegisterResponse {
    @Setter private SuccessResponse successResponse;
    private ErrorResponse errorResponse;

    public void setErrorResponse(String message) {
        errorResponse = new ErrorResponse(message);
    }

    @Getter
    @RequiredArgsConstructor
    public static class SuccessResponse {
        private final Long userId;
        private final Long customerId;
        private final String fullName;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }
}
