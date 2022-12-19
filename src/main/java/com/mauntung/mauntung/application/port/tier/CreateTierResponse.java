package com.mauntung.mauntung.application.port.tier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateTierResponse {
    private final Long id;
    private final String name;
    private final Integer rewardsQty;
    private final Integer requiredPoints;
    private final Float multiplierFactor;
}
