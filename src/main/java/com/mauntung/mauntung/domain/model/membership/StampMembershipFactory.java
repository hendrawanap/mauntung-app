package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Date;
import java.util.Set;

public interface StampMembershipFactory {
    StampMembershipBuilder builder(String name, Merchant merchant, Set<Reward> rewards, Date createdAt, StampRules rules, boolean isFinalized);
}
