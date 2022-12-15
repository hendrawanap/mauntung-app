package com.mauntung.mauntung.application.port.reward;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
public class CreateRewardResponse {
    @Setter private SuccessResponse successResponse;
    private ErrorResponse errorResponse;

    public void setErrorResponse(String message) {
        errorResponse = new ErrorResponse(message);
    }

    @RequiredArgsConstructor
    public static class SuccessResponse {
        private final Long id;
        private final Date createdAt;
    }

    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }
}
