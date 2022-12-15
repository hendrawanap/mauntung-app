package com.mauntung.mauntung.application.port.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class CreateStampMembershipCommand {
    private final String name;
    private final long userId;
    private final Set<Long> rewardIds;
    private final int redeemTtl;
    private final int usableDuration;
    private final int cardCapacity;
}
