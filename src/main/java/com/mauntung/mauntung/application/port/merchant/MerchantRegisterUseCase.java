package com.mauntung.mauntung.application.port.merchant;

public interface MerchantRegisterUseCase {
    MerchantRegisterResponse apply(MerchantRegisterCommand command);
}
