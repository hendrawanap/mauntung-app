package com.mauntung.mauntung.adapter.persistence.membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mauntung.mauntung.adapter.persistence.merchant.MerchantMapper;
import com.mauntung.mauntung.adapter.persistence.reward.RewardMapper;
import com.mauntung.mauntung.adapter.persistence.tier.TierEntity;
import com.mauntung.mauntung.adapter.persistence.tier.TierMapper;
import com.mauntung.mauntung.domain.model.membership.*;

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
        if (entity.getType().equals("point")) {
            PointMembershipBuilder builder = pointMembershipFactory.builder(
                entity.getName(),
                merchantMapper.entityToModel(entity.getMerchant()),
                entity.getRewards().stream().map(rewardMapper::entityToModel).collect(Collectors.toSet()),
                entity.getCreatedAt(),
                jsonMapper.readValue(entity.getRules(), PointRules.class)
            ).id(entity.getId());
            Set<TierEntity> tierEntities = entity.getTiers();
            if (tierEntities != null) {
                builder.tiers(tierEntities.stream().map(tierMapper::entityToModel).collect(Collectors.toSet()));
            }
            membership = builder.build();
        } else if (entity.getType().equals("stamp")) {
            StampMembershipBuilder builder = stampMembershipFactory.builder(
                entity.getName(),
                merchantMapper.entityToModel(entity.getMerchant()),
                entity.getRewards().stream().map(rewardMapper::entityToModel).collect(Collectors.toSet()),
                entity.getCreatedAt(),
                jsonMapper.readValue(entity.getRules(), StampRules.class)
            ).id(entity.getId());
            membership = builder.build();
        }
        return membership;
    }

    public MembershipEntity modelToEntity(Membership membership) throws JsonProcessingException {
        MembershipEntity.MembershipEntityBuilder builder = MembershipEntity.builder()
            .id(membership.getId())
            .name(membership.getName())
            .merchant(merchantMapper.modelToEntity(membership.getMerchant()))
            .rewards(membership.getRewards().stream().map(rewardMapper::modelToEntity).collect(Collectors.toSet()));
        if (membership instanceof PointMembership) {
            builder.type("point");
            builder.rules(jsonMapper.writeValueAsString(((PointMembership) membership).getRules()));
            Set<Tier> tiers = ((PointMembership) membership).getTiers();
            if (tiers != null) {
                builder.tiers(tiers.stream().map(tierMapper::modelToEntity).collect(Collectors.toSet()));
            }
        } else if (membership instanceof StampMembership) {
            builder.type("stamp");
            builder.rules(jsonMapper.writeValueAsString(((StampMembership) membership).getRules()));
        } else {
            return null;
        }
        return builder.build();
    }
}
