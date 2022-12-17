package com.mauntung.mauntung.application.port.tier;

import com.mauntung.mauntung.domain.model.membership.Tier;

import java.util.Optional;
import java.util.Set;

public interface TierRepository {
    Optional<Long> save(Tier tier);

    Set<Tier> findAllById(Set<Long> ids);
}
