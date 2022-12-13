package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

@Getter
public class StampRules {
    private final int redeemTtl;
    private final int usableDuration;
    private final int cardCapacity;

    public StampRules(int redeemTtl, int usableDuration, int cardCapacity) throws IllegalArgumentException {
        validate(redeemTtl, usableDuration, cardCapacity);
        this.redeemTtl = redeemTtl;
        this.usableDuration = usableDuration;
        this.cardCapacity = cardCapacity;
    }

    private static void validate(int redeemTtl, int usableDuration, int cardCapacity) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (redeemTtl < 0) mb.append("Redeem TTL must not be negative value");

        if (usableDuration < 0) mb.append("Usable Duration must not be negative value");

        if (cardCapacity < 1) mb.append("Card capacity must not be less than 1");
        else if (cardCapacity > 20) mb.append("Card capacity must not be more than 20");

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }
}
