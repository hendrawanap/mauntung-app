package com.mauntung.mauntung.domain.model.customer_membership;

import com.mauntung.mauntung.domain.model.membership.PointMembership;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.point.Point;
import com.mauntung.mauntung.domain.model.point.PointFactory;
import com.mauntung.mauntung.domain.model.point.PointFactoryImpl;
import com.mauntung.mauntung.domain.model.redeem.Redeem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerPointMembershipTest {
    static PointFactory pointFactory;
    static CustomerPointMembershipFactory membershipFactory;
    static Date now;
    static Set<Point> points;
    static Set<Redeem> redeems;
    static Merchant merchant;

    @BeforeAll
    static void beforeAll() {
        pointFactory = new PointFactoryImpl();
        membershipFactory = new CustomerPointMembershipFactoryImpl();
        now = new Date();
        redeems = Set.of();
        PointMembership pointMembership = mock(PointMembership.class);

        merchant = mock(Merchant.class);
        when(merchant.getName()).thenReturn("Merchant");
        when(merchant.getMembership()).thenReturn(pointMembership);

        Date tenDaysAgo = new Date(now.toInstant().minus(10, ChronoUnit.DAYS).toEpochMilli());
        Date twoYearsAgo = new Date(now.toInstant().minus(730, ChronoUnit.DAYS).toEpochMilli());

        Point unusablePoint = mock(Point.class);
        when(unusablePoint.isUsable()).thenReturn(false);
        when(unusablePoint.getCurrentValue()).thenReturn(10);
        when(unusablePoint.getClaimedValue()).thenReturn(10);
        when(unusablePoint.getClaimedAt()).thenReturn(tenDaysAgo);

        Point twoYearsAgoPoint = mock(Point.class);
        when(twoYearsAgoPoint.isUsable()).thenReturn(false);
        when(twoYearsAgoPoint.getCurrentValue()).thenReturn(10);
        when(twoYearsAgoPoint.getClaimedValue()).thenReturn(10);
        when(twoYearsAgoPoint.getClaimedAt()).thenReturn(twoYearsAgo);

        points = new HashSet<>();
        points.addAll(List.of(twoYearsAgoPoint, unusablePoint));

        for (long i = 0; i < 5; i++) {
            Point usablePoint = mock(Point.class);
            when(usablePoint.getId()).thenReturn(i);
            when(usablePoint.isUsable()).thenReturn(true);
            when(usablePoint.getCurrentValue()).thenReturn(10);
            when(usablePoint.getClaimedValue()).thenReturn(10);
            when(usablePoint.getClaimedAt()).thenReturn(now);

            points.add(usablePoint);
        }
    }

    @Test
    void getBalance_shouldReturn50() {
        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points).build();
        assertEquals(50, membership.getBalance());
    }

    @Test
    void getTotalPointsClaimedInYear_shouldReturn60() {
        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points).build();
        assertEquals(60, membership.getTotalPointsClaimedInYear());
    }

    @Test
    void givenNullTiers_getCurrentTier_shouldReturnNull() {
        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points).build();
        assertNull(membership.getCurrentTier());
    }

    @Test
    void given60Points_getCurrentTier_shouldReturnSilverTier() {
        Tier bronzeTier = mock(Tier.class);
        when(bronzeTier.getRequiredPoints()).thenReturn(0);

        Tier silverTier = mock(Tier.class);
        when(silverTier.getRequiredPoints()).thenReturn(50);

        Tier goldTier = mock(Tier.class);
        when(goldTier.getRequiredPoints()).thenReturn(100);

        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points)
            .tiers(Set.of(goldTier, silverTier, bronzeTier))
            .build();

        assertEquals(silverTier, membership.getCurrentTier());
    }

    @Test
    void getSortedTierList() {
        Tier bronzeTier = mock(Tier.class);
        when(bronzeTier.getRequiredPoints()).thenReturn(0);

        Tier silverTier = mock(Tier.class);
        when(silverTier.getRequiredPoints()).thenReturn(50);

        Tier goldTier = mock(Tier.class);
        when(goldTier.getRequiredPoints()).thenReturn(100);

        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points)
            .tiers(Set.of(goldTier, silverTier, bronzeTier))
            .build();

        assertEquals(bronzeTier, membership.getSortedTierList().get(0));
        assertEquals(silverTier, membership.getSortedTierList().get(1));
        assertEquals(goldTier, membership.getSortedTierList().get(2));
    }

    @Test
    void givenAmount30_deductBalance_shouldReturn3ChangedPoints() {
        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points).build();
        List<Point> changedPoints = membership.deductBalance(30);
        assertEquals(3, changedPoints.size());
    }

    @Test
    void givenAmount100_deductBalance_shouldReturnNull() {
        CustomerPointMembership membership = membershipFactory.builder(merchant, now, redeems, points).build();
        List<Point> changedPoints = membership.deductBalance(100);
        assertNull(changedPoints);
    }
}