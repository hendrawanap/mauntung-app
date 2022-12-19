package com.mauntung.mauntung.application.port.customer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerRegisterResponse {
    private final Long userId;
    private final Long customerId;
    private final String fullName;
}
