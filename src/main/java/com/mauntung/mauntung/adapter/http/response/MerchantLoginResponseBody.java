package com.mauntung.mauntung.adapter.http.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
public class MerchantLoginResponseBody {
    private final Data data;

    public MerchantLoginResponseBody(Long userId, String token, Date expiredAt) {
        data = new Data(userId, token, expiredAt);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Data {
        private final Long userId;
        private final String token;
        private final Date expiredAt;
    }
}
