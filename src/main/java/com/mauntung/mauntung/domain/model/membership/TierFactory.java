package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Set;

public interface TierFactory {
    TierBuilder builder(String name, Set<Reward> rewards, int requiredPoints);
}
