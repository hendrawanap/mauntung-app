package com.mauntung.mauntung.application.port.merchant;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class MerchantRegisterResponse {
    @Setter private SuccessResponse successResponse;
    private ErrorResponse errorResponse;

    public void setErrorResponse(String message) {
        errorResponse = new ErrorResponse(message);
    }

    @Getter
    @Builder
    public static class SuccessResponse {
        private Long userId;
        private Long merchantId;
        private String merchantName;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ErrorResponse {
        private final String message;
    }
}
