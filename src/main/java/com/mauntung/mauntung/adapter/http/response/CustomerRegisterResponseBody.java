package com.mauntung.mauntung.adapter.http.response;

import com.mauntung.mauntung.application.port.customer.CustomerRegisterResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CustomerRegisterResponseBody {
    private final Data data;

    public CustomerRegisterResponseBody(CustomerRegisterResponse response) {
        data = new Data(
            response.getUserId(),
            response.getCustomerId(),
            response.getFullName()
        );
    }

    @Getter
    @RequiredArgsConstructor
    public static class Data {
        private final Long userId;
        private final Long customerId;
        private final String fullName;
    }
}
