package com.mauntung.mauntung.application.port.reward;

import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Optional;
import java.util.Set;

public interface RewardRepository {
    Optional<Long> save(Reward reward);

    Set<Reward> findAllById(Set<Long> ids);
}
