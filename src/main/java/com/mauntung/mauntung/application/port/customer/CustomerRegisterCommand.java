package com.mauntung.mauntung.application.port.customer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomerRegisterCommand {
    private final String email;
    private final String password;
    private final String fullName;
}
