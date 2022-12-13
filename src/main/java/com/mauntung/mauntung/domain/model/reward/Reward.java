package com.mauntung.mauntung.domain.model.reward;

import lombok.*;

import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Reward {
    public static final String DUMMY_IMG_URL = "https://via.placeholder.com/120";

    private final Long id;
    private final String name;
    private final String description;
    private final String termsCondition;
    private final int cost;
    private final String imgUrl;
    private final Integer stock;
    private final Date startPeriod;
    private final Date endPeriod;

    public boolean isClaimable() {
        boolean stockIsAvailable = stock == null || stock != 0;
        Date now = new Date();
        return stockIsAvailable && isStarted(now) && !isEnded(now);
    }

    private boolean isStarted(Date now) {
        if (startPeriod == null) return true;
        return now.equals(startPeriod) || now.after(startPeriod);
    }

    private boolean isEnded(Date now) {
        if (endPeriod == null) return false;
        return now.equals(endPeriod) || now.after(endPeriod);
    }
}
