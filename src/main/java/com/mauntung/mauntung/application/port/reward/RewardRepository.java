package com.mauntung.mauntung.application.port.reward;

import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Optional;

public interface RewardRepository {
    Optional<Long> save(Reward reward);
}
