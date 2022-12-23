package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.*;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Tier {
    public static final float DEFAULT_MULTIPLIER_FACTOR = 1;

    private final Long id;
    private final String name;
    private final Set<Reward> rewards;
    private final int requiredPoints;
    private final float multiplierFactor;
}
