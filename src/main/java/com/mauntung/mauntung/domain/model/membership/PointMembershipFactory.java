package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;

import java.util.Date;
import java.util.Set;

public interface PointMembershipFactory {
    PointMembershipBuilder builder(String name, Set<Reward> rewards, Date createdAt, PointRules rules, boolean isFinalized);
}
