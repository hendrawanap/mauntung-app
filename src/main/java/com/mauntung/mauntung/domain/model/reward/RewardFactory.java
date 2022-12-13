package com.mauntung.mauntung.domain.model.reward;

public interface RewardFactory {
    RewardBuilder builder(String name, String description, String termsCondition, int cost);

    Reward createCopyWithNewStock(Reward oldReward, int newStock);
}
