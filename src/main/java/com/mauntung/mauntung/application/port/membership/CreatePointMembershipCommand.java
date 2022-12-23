package com.mauntung.mauntung.application.port.membership;

import com.mauntung.mauntung.domain.model.membership.PointRules;
import lombok.Getter;

import java.util.Set;

@Getter
public class CreatePointMembershipCommand {
    private final String name;
    private final Long userId;
    private final Set<Long> rewardIds;
    private final PointRules rules;
    private final Set<Long> tierIds;

    public CreatePointMembershipCommand(String name, Long userId, Set<Long> rewardIds, PointRules rules, Set<Long> tierIds) {
        this.name = name;
        this.userId = userId;
        this.rewardIds = rewardIds;
        this.rules = rules;
        this.tierIds = tierIds;
    }

    public CreatePointMembershipCommand(String name, Long userId, Set<Long> rewardIds, PointRules rules) {
        this(name, userId, rewardIds, rules, null);
    }
}
