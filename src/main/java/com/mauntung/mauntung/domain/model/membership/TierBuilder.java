package com.mauntung.mauntung.domain.model.membership;

public interface TierBuilder {
    Tier build();

    TierBuilder id(Long id);

    TierBuilder multiplierFactor(Float multiplierFactor);
}
