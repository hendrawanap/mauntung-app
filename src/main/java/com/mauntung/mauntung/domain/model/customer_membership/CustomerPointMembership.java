package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.point.Point;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomerPointMembership implements CustomerMembership {
    @Getter private final Long id;
    @Getter private final String name;
    @Getter private final String merchantName;
    @Getter private final Date joinedAt;
    @Getter private final String img;
    @Getter private final Set<Redeem> redeems;
    private final Set<Point> points;
    @Getter private final Set<Tier> tiers;

    @Override
    public int getBalance() {
        return points.stream()
            .filter(Point::isUsable)
            .map(Point::getCurrentValue)
            .reduce(Integer::sum)
            .orElseThrow();
    }

    public int getTotalPointsClaimedInYear() {
        Date yearAgo = new Date(new Date().toInstant().minus(365, ChronoUnit.DAYS).toEpochMilli());
        return points.stream()
            .filter(point -> point.getClaimedAt().after(yearAgo))
            .map(Point::getClaimedValue)
            .reduce(Integer::sum)
            .orElseThrow();
    }

    public List<Tier> getSortedTierList() {
        if (tiers == null) return null;

        return tiers.stream()
            .sorted(Comparator.comparingInt(Tier::getRequiredPoints))
            .collect(Collectors.toList());
    }

    public Tier getCurrentTier() {
        if (tiers == null) return null;

        List<Tier> tierList = getSortedTierList();
        int totalPointsClaimedInYear = getTotalPointsClaimedInYear();
        Tier currentTier = null;

        for (Tier tier : tierList) {
            if (totalPointsClaimedInYear < tier.getRequiredPoints()) break;
            currentTier = tier;
        }

        return currentTier;
    }

    public Set<Point> deductBalance(int amount) {
        if (amount > getBalance()) return null;

        List<Point> usablePoints = points.stream()
            .filter(Point::isUsable)
            .sorted(Comparator.comparing(Point::getClaimedAt))
            .collect(Collectors.toList());

        Set<Point> usedPoints = new HashSet<>();
        int remaining = amount;

        for (Point point : usablePoints) {
            if (remaining == 0) break;

            int diff = point.getCurrentValue() - remaining;

            if (diff < 0) {
                point.setCurrentValue(0);
                remaining -= point.getCurrentValue();
            } else {
                point.setCurrentValue(diff);
                remaining = 0;
            }

            usedPoints.add(point);
        }

        return usedPoints;
    }
}
