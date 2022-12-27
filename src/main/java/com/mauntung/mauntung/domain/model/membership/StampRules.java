package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.common.MessageBuilder;
import lombok.Getter;

import java.beans.ConstructorProperties;

@Getter
public class StampRules {
    private final int redeemTtl;
    private final int usableDuration;
    private final int cardCapacity;

    @ConstructorProperties({"redeemTtl", "usableDuration", "cardCapacity"})
    public StampRules(int redeemTtl, int usableDuration, int cardCapacity) throws IllegalArgumentException {
        validate(redeemTtl, usableDuration, cardCapacity);
        this.redeemTtl = redeemTtl;
        this.usableDuration = usableDuration;
        this.cardCapacity = cardCapacity;
    }

    private static void validate(int redeemTtl, int usableDuration, int cardCapacity) throws IllegalArgumentException {
        MessageBuilder mb = new MessageBuilder();

        if (redeemTtl < 1) mb.append("Redeem TTL must be larger than 0");

        if (usableDuration < 1) mb.append("Usable Duration must be larger than 0");

        if (cardCapacity < 1) mb.append("Card capacity must be larger than 0");
        else if (cardCapacity > 20) mb.append("Card capacity must not be more than 20");

        if (!mb.isEmpty()) throw new IllegalArgumentException(mb.toString());
    }
}
