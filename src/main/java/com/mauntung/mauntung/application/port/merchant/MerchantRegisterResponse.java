package com.mauntung.mauntung.application.port.merchant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MerchantRegisterResponse {
    private final Long userId;
    private final Long merchantId;
    private final String merchantName;
}
