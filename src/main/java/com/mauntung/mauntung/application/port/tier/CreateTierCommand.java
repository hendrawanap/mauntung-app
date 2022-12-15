package com.mauntung.mauntung.application.port.tier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class CreateTierCommand {
    private final String name;
    private final Set<Long> rewardIds;
    private final int requiredPoints;
    private final float multiplierFactor;
}
