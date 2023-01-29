package com.mauntung.mauntung.adapter.persistence.merchant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.adapter.persistence.membership.MembershipEntity;
import com.mauntung.mauntung.adapter.persistence.membership.MembershipMapper;
import com.mauntung.mauntung.adapter.persistence.user.UserEntity;
import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactory;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactoryImpl;

public class MerchantMapper {
    private final MerchantFactory factory = new MerchantFactoryImpl();
    private final MembershipMapper membershipMapper = new MembershipMapper();

    public Merchant entityToModel(MerchantEntity entity) {
        Membership membership = null;
        try {
            boolean hasMembership = entity.getMembership() != null;
            if (hasMembership) {
                membership = membershipMapper.entityToModel(entity.getMembership());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return factory.builder(entity.getName(), entity.getCreatedAt())
            .id(entity.getId())
            .phone(entity.getPhone())
            .membership(membership)
            .build();
    }

    public MerchantEntity modelToEntity(Merchant model) {
        MembershipEntity membership = null;
        try {
            if (model.hasCreatedMembership()) {
                membership = membershipMapper.modelToEntity(model.getMembership());
                membership.setMerchant(MerchantEntity.builder().id(model.getId()).build());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return MerchantEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .phone(model.getPhone())
            .membership(membership)
            .build();
    }

    public MerchantEntity modelToEntity(Merchant model, Long userId) {
        MembershipEntity membership = null;
        try {
            if (model.hasCreatedMembership()) {
                membership = membershipMapper.modelToEntity(model.getMembership());
                membership.setMerchant(MerchantEntity.builder().id(model.getId()).build());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return MerchantEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .phone(model.getPhone())
            .user(UserEntity.builder().id(userId).build())
            .membership(membership)
            .build();
    }
}
