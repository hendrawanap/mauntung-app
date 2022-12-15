package com.mauntung.mauntung.application.port.tier;

import com.mauntung.mauntung.domain.model.membership.Tier;

import java.util.Optional;

public interface TierRepository {
    Optional<Long> save(Tier tier);
}
