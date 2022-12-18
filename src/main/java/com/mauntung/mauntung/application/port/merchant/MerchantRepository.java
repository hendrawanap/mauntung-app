package com.mauntung.mauntung.application.port.merchant;

import com.mauntung.mauntung.domain.model.merchant.Merchant;

import java.util.Optional;

public interface MerchantRepository {
    Optional<Merchant> findByUserId(Long userId);

    Optional<Long> save(Merchant merchant);

    Optional<Long> save(Merchant merchant, Long userId);
}
