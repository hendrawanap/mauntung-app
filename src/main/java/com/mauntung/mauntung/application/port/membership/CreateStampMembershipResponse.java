package com.mauntung.mauntung.application.port.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class CreateStampMembershipResponse {
    private final long id;
    private final String name;
    private final int rewardsQty;
    private final int cardCapacity;
    private final Date createdAt;
}
