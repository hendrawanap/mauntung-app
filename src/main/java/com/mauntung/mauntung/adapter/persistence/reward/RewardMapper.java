package com.mauntung.mauntung.adapter.persistence.reward;

import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;

public class RewardMapper {
    private final RewardFactory factory = new RewardFactoryImpl();

    public Reward entityToModel(RewardEntity entity) {
        return factory.builder(
            entity.getName(),
            entity.getDescription(),
            entity.getTermsCondition(),
            entity.getCost()
        )
            .stock(entity.getStock())
            .startPeriod(entity.getStartPeriod())
            .endPeriod(entity.getEndPeriod())
            .build();
    }

    public RewardEntity modelToEntity(Reward model) {
        return RewardEntity.builder()
            .id(model.getId())
            .name(model.getName())
            .description(model.getDescription())
            .termsCondition(model.getTermsCondition())
            .cost(model.getCost())
            .stock(model.getStock())
            .startPeriod(model.getStartPeriod())
            .endPeriod(model.getEndPeriod())
            .build();
    }
}
