package com.mauntung.mauntung.adapter.persistence.merchant;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface JpaMerchantRepository extends CrudRepository<MerchantEntity, Long> {
    Optional<MerchantEntity> findByUserId(Long userId);
}
