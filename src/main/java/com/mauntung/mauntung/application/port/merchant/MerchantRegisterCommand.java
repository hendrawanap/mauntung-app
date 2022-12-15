package com.mauntung.mauntung.application.port.merchant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MerchantRegisterCommand {
    private final String email;
    private final String password;
    private final String merchantName;
}
