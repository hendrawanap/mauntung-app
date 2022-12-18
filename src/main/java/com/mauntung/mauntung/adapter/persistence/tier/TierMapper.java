package com.mauntung.mauntung.adapter.persistence.tier;

import com.mauntung.mauntung.adapter.persistence.reward.RewardEntity;
import com.mauntung.mauntung.adapter.persistence.reward.RewardMapper;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Set;
import java.util.stream.Collectors;

public class TierMapper {
    private final RewardMapper rewardMapper = new RewardMapper();

    public Tier entityToModel(TierEntity entity) {
        Set<Reward> rewards = entity.getRewards()
            .stream()
            .map(rewardMapper::entityToModel)
            .collect(Collectors.toSet());
        return Tier.withId(entity.getName(), rewards, entity.getRequiredPoints(), entity.getMultiplierFactor(), entity.getId());
    }

    public TierEntity modelToEntity(Tier model) {
        Set<RewardEntity> rewardEntities = model.getRewards()
            .stream()
            .map(rewardMapper::modelToEntity)
            .collect(Collectors.toSet());
        return TierEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .rewards(rewardEntities)
            .requiredPoints(model.getRequiredPoints())
            .multiplierFactor(model.getMultiplierFactor())
            .build();
    }
}
