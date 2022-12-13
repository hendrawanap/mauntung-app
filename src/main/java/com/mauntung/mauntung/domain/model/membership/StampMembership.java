package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class StampMembership implements Membership {
    private final Long id;
    private final String name;
    private final Merchant merchant;
    private final Set<Reward> rewards;
    private final Date createdAt;
    private final String img;
    private final StampRules rules;

    @Override
    public Integer getRewardsQty() {
        return rewards.size();
    }
}
