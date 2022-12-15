package com.mauntung.mauntung.application.port.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
public class CreateStampMembershipResponse {
    @Setter private SuccessResponse successResponse;
    private ErrorResponse errorResponse;

    public void setErrorResponse(String message) {
        errorResponse = new ErrorResponse(message);
    }

    @Getter
    @RequiredArgsConstructor
    public static class SuccessResponse {
        private final long id;
        private final String name;
        private final int rewardsQty;
        private final int cardCapacity;
        private final Date createdAt;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }
}
