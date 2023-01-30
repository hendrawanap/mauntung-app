package com.mauntung.mauntung.domain.model.point;

import lombok.*;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Point {
    private final Long id;
    private final int baseValue;
    private final Integer claimedValue;
    @Setter private Integer currentValue;
    private final int claimableDuration;
    private final int usableDuration;
    private final Date claimedAt;
    private final Date createdAt;
    private final UUID code;

    public Date claimableBefore() {
        return new Date(createdAt.toInstant().plus(claimableDuration, ChronoUnit.DAYS).toEpochMilli());
    }

    public Date usableBefore() {
        if (claimedAt == null) return null;
        return new Date(claimedAt.toInstant().plus(usableDuration, ChronoUnit.DAYS).toEpochMilli());
    }

    public boolean isClaimed() {
        return claimedAt != null && claimedValue != null;
    }

    public boolean isClaimable() {
        Date now = new Date();
        boolean claimablePeriodPassed = now.after(claimableBefore());
        return !(isClaimed() || claimablePeriodPassed);
    }

    public boolean isUsable() {
        if (!isClaimed()) return false;
        Date now = new Date();
        boolean usablePeriodPassed = now.after(usableBefore());
        return currentValue > 0 && !usablePeriodPassed;
    }
}
