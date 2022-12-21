package com.mauntung.mauntung.adapter.http.response;

import com.mauntung.mauntung.application.port.merchant.MerchantRegisterResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class MerchantRegisterResponseBody {
    private final Data data;

    public MerchantRegisterResponseBody(MerchantRegisterResponse response) {
        data = new Data(
            response.getUserId(),
            response.getMerchantId(),
            response.getMerchantName()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class Data {
        private final Long userId;
        private final Long merchantId;
        private final String merchantName;
    }
}
