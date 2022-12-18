package com.mauntung.mauntung.adapter.persistence.merchant;

import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactory;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactoryImpl;

public class MerchantMapper {
    private final MerchantFactory factory = new MerchantFactoryImpl();

    public Merchant entityToModel(MerchantEntity entity) {
        return factory.builder(entity.getName(), entity.getCreatedAt())
            .id(entity.getId())
            .phone(entity.getPhone())
            .build();
    }

    public MerchantEntity modelToEntity(Merchant model) {
        return MerchantEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .phone(model.getPhone())
            .build();
    }

    public MerchantEntity modelToEntity(Merchant model, Long userId) {
        return MerchantEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .phone(model.getPhone())
            .user(UserEntity.builder().id(userId).build())
            .build();
    }
}
