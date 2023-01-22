package com.mauntung.mauntung.adapter.persistence.membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.adapter.persistence.merchant.MerchantEntity;
import com.mauntung.mauntung.adapter.persistence.merchant.MerchantMapper;
import com.mauntung.mauntung.adapter.persistence.reward.RewardEntity;
import com.mauntung.mauntung.adapter.persistence.reward.RewardMapper;
import com.mauntung.mauntung.adapter.persistence.tier.TierEntity;
import com.mauntung.mauntung.adapter.persistence.tier.TierMapper;
import com.mauntung.mauntung.domain.model.membership.*;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Set;
import java.util.stream.Collectors;

public class MembershipMapper {
    private final PointMembershipFactory pointMembershipFactory = new PointMembershipFactoryImpl();
    private final StampMembershipFactory stampMembershipFactory = new StampMembershipFactoryImpl();
    private final MerchantMapper merchantMapper = new MerchantMapper();
    private final RewardMapper rewardMapper = new RewardMapper();
    private final TierMapper tierMapper = new TierMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper();

    public Membership entityToModel(MembershipEntity entity) throws JsonProcessingException {
        Membership membership = null;
        boolean isPointMembership = entity.getType().equalsIgnoreCase(Membership.Type.POINT.toString());
        boolean isStampMembership = entity.getType().equalsIgnoreCase(Membership.Type.STAMP.toString());

        if (isPointMembership) {
            membership = buildPointMembership(entity);
        } else if (isStampMembership) {
            membership = buildStampMembership(entity);
        }

        return membership;
    }

    private Membership buildPointMembership(MembershipEntity entity) throws JsonProcessingException {
        Merchant merchant = merchantMapper.entityToModel(entity.getMerchant());
        PointRules rules = jsonMapper.readValue(entity.getRules(), PointRules.class);
        Set<TierEntity> tierEntities = entity.getTiers();
        Set<Reward> rewards = entity.getRewards()
            .stream()
            .map(rewardMapper::entityToModel)
            .collect(Collectors.toSet());

        PointMembershipBuilder builder = pointMembershipFactory.builder(
            entity.getName(),
            merchant,
            rewards,
            entity.getCreatedAt(),
            rules,
            entity.getIsFinalized()
        );

        builder.id(entity.getId());

        if (tierEntities != null) {
            builder.tiers(tierEntities.stream()
                .map(tierMapper::entityToModel)
                .collect(Collectors.toSet())
            );
        }

        return builder.build();
    }

    private Membership buildStampMembership(MembershipEntity entity) throws JsonProcessingException {
        Merchant merchant = merchantMapper.entityToModel(entity.getMerchant());
        StampRules rules = jsonMapper.readValue(entity.getRules(), StampRules.class);
        Set<Reward> rewards = entity.getRewards()
            .stream()
            .map(rewardMapper::entityToModel)
            .collect(Collectors.toSet());

        StampMembershipBuilder builder = stampMembershipFactory.builder(
            entity.getName(),
            merchant,
            rewards,
            entity.getCreatedAt(),
            rules,
            entity.getIsFinalized()
        );

        builder.id(entity.getId());

        return builder.build();
    }

    public MembershipEntity modelToEntity(Membership membership) throws JsonProcessingException {
        MembershipEntity.MembershipEntityBuilder builder = MembershipEntity.builder();

        if (membership instanceof PointMembership) {
            attachPointMembershipFields((PointMembership) membership, builder);
        } else if (membership instanceof StampMembership) {
            attachStampMembershipFields((StampMembership) membership, builder);
        } else {
            return null;
        }

        MerchantEntity merchantEntity = merchantMapper.modelToEntity(membership.getMerchant());
        Set<RewardEntity> rewardEntities = membership.getRewards()
            .stream()
            .map(rewardMapper::modelToEntity)
            .collect(Collectors.toSet());

        return builder.id(membership.getId())
            .name(membership.getName())
            .merchant(merchantEntity)
            .rewards(rewardEntities)
            .build();
    }

    private void attachPointMembershipFields(PointMembership membership, MembershipEntity.MembershipEntityBuilder builder) throws JsonProcessingException {
        String jsonRules = jsonMapper.writeValueAsString(membership.getRules());
        Set<Tier> tiers = membership.getTiers();

        builder.rules(jsonRules)
            .type(Membership.Type.POINT.toString())
            .isFinalized(membership.isFinalized());

        if (tiers != null) {
            builder.tiers(tiers.stream()
                .map(tierMapper::modelToEntity)
                .collect(Collectors.toSet())
            );
        }
    }

    private void attachStampMembershipFields(StampMembership membership, MembershipEntity.MembershipEntityBuilder builder) throws JsonProcessingException {
        String jsonRules = jsonMapper.writeValueAsString(membership.getRules());

        builder.rules(jsonRules)
            .type(Membership.Type.STAMP.toString())
            .isFinalized(membership.isFinalized());
    }
}
