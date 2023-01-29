package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PointMembership implements Membership {
    private final Long id;
    private final String name;
    private final Set<Reward> rewards;
    private final Date createdAt;
    private final PointRules rules;
    private final String img;
    private final Set<Tier> tiers;
    private boolean isFinalized;

    @Override
    public Integer getRewardsQty() {
        return rewards.size();
    }

    @Override
    public void confirmFinalize() {
        boolean isValidState = !isFinalized && isComplete();
        if (!isValidState) {
            MessageBuilder mb = new MessageBuilder();

            if (isFinalized)
                mb.append("membership has already finalized");

            if (!isComplete())
                mb.append("membership is not in complete state");

            throw new IllegalStateException(String.format("Unable to finalize membership: %s", mb));
        }
        isFinalized = true;
    }

    public Integer getTiersQty() {
        if (tiers == null) return null;
        return tiers.size();
    }

    private boolean isComplete() {
        return !name.isBlank();
    }
}
