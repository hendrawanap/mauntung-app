package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.redeem.Redeem;
import com.mauntung.mauntung.domain.model.stamp.Stamp;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomerStampMembership implements CustomerMembership {
    @Getter private final Long id;
    @Getter private final String name;
    @Getter private final String merchantName;
    @Getter private final Date joinedAt;
    @Getter private final String img;
    @Getter private final Set<Redeem> redeems;
    private final List<Stamp> stamps;
    @Getter private final int cardCapacity;

    @Override
    public int getBalance() {
        return (int) stamps.stream().filter(Stamp::isUsable).count();
    }

    public boolean isFull() {
        return getBalance() >= cardCapacity;
    }

    public List<Stamp> deductBalance(int amount) {
        if (amount > getBalance()) return null;

        List<Stamp> usableStamps = stamps.stream()
            .filter(Stamp::isUsable)
            .sorted(Comparator.comparing(Stamp::getCreatedAt))
            .collect(Collectors.toList());

        List<Stamp> usedStamps = new ArrayList<>();
        int remaining = amount;

        for (Stamp stamp : usableStamps) {
            if (remaining == 0) break;
            stamp.setUsedAt(new Date());
            usedStamps.add(stamp);
            remaining--;
        }

        return usedStamps;
    }
}
