package com.mauntung.mauntung.domain.model.redeem;

import com.mauntung.mauntung.domain.model.reward.Reward;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Redeem {
    private final Long id;
    private final String name;
    private final String description;
    private final String termsCondition;
    private final int cost;
    private final UUID code;
    private final Reward reward;
    private final Date createdAt;
    private final Date expiredAt;
    private final Date usedAt;

    public boolean isExpired() {
        return new Date().after(expiredAt);
    }

    public boolean isUsed() {
        return usedAt != null;
    }
}
