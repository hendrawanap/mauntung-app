package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.*;

import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tier {
    private final Long id;
    private final String name;
    private final Set<Reward> rewards;
    private final int requiredPoints;
    private final float multiplierFactor;

    public static Tier withId(String name, Set<Reward> rewards, int requiredPoints, float multiplierFactor, Long id) {
        validate(requiredPoints, multiplierFactor);
        return new Tier(id, name, rewards, requiredPoints, multiplierFactor);
    }

    public static Tier withoutId(String name, Set<Reward> rewards, int requiredPoints, float multiplierFactor) {
        validate(requiredPoints, multiplierFactor);
        return new Tier(null, name, rewards, requiredPoints, multiplierFactor);
    }

    private static void validate(int requiredPoints, float multiplierFactor) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (requiredPoints < 0) mb.append("Required Points must not be negative value");

        if (multiplierFactor < 0) mb.append("Multiplier Factor must not be negative value");

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }
}
