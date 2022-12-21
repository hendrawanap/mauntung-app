package com.mauntung.mauntung.adapter.http.response.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
public class CustomerLoginResponseBody {
    private final Data data;

    public CustomerLoginResponseBody(Long userId, String token, Date expiredAt) {
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
