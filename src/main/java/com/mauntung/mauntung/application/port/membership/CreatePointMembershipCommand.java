package com.mauntung.mauntung.application.port.membership;

import com.mauntung.mauntung.domain.model.membership.PointRules;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Getter
public class CreatePointMembershipCommand {
    private final String name;
    private final Long userId;
    private final Set<Long> rewardIds;
    private final Date createdAt;
    private final PointRules rules;
    private final Set<Long> tierIds;

    public CreatePointMembershipCommand(String name, Long userId, Set<Long> rewardIds, Date createdAt, PointRules rules, Set<Long> tierIds) {
        this.name = name;
        this.userId = userId;
        this.rewardIds = rewardIds;
        this.createdAt = createdAt;
        this.rules = rules;
        this.tierIds = tierIds;
    }

    public CreatePointMembershipCommand(String name, Long userId, Set<Long> rewardIds, Date createdAt, PointRules rules) {
        this(name, userId, rewardIds, createdAt, rules, null);
    }
}
