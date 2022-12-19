package com.mauntung.mauntung.application.port.membership;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Getter
@RequiredArgsConstructor
public class CreatePointMembershipResponse {
    private final Long id;
    private final String name;
    private final Integer rewardsQty;
    private final Integer tiersQty;
    private final Date createdAt;
}
