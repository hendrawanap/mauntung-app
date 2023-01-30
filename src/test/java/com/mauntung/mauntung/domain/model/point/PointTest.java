package com.mauntung.mauntung.domain.model.point;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    static PointFactory pointFactory;
    static int baseValue;
    static int claimableDuration;
    static int usableDuration;
    static Date createdAt;
    static UUID code;

    @BeforeAll
    static void beforeAll() {
        pointFactory = new PointFactoryImpl();
        baseValue = 10;
        claimableDuration = 10;
        usableDuration = 10;
        createdAt = new Date();
        code = UUID.randomUUID();
    }

    @Test
    void givenNonNullClaimedAt_isClaimed_shouldReturnTrue() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code)
            .claimedValue(new ClaimedValue(baseValue, createdAt))
            .currentValue(baseValue)
            .build();
        assertTrue(point.isClaimed());
    }

    @Test
    void givenNullClaimedAt_isClaimed_shouldReturnFalse() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code).build();
        assertFalse(point.isClaimed());
    }

    @Test
    void isClaimable_shouldReturnTrue() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code).build();
        assertTrue(point.isClaimable());
    }

    @Test
    void givenNonNullClaimedAt_isClaimable_shouldReturnFalse() {
        Point point1 = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code)
            .claimedValue(new ClaimedValue(baseValue, createdAt))
            .currentValue(baseValue)
            .build();
        assertFalse(point1.isClaimable());
    }

    @Test
    void givenPastClaimablePeriod_isClaimable_shouldReturnFalse() {
        Date pastDate = new Date(
            createdAt.toInstant()
                .minus(claimableDuration + 1, ChronoUnit.DAYS)
                .toEpochMilli()
        );
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, pastDate, code).build();
        assertFalse(point.isClaimable());
    }

    @Test
    void isUsable_shouldReturnTrue() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code)
            .claimedValue(new ClaimedValue(baseValue, createdAt))
            .currentValue(baseValue)
            .build();
        assertTrue(point.isUsable());
    }

    @Test
    void givenNullClaimedAt_isUsable_shouldReturnFalse() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code).build();
        assertFalse(point.isUsable());
    }

    @Test
    void givenPastUsablePeriod_isUsable_shouldReturnFalse() {
        Date pastDate = new Date(
            createdAt.toInstant()
                .minus(usableDuration + 1, ChronoUnit.DAYS)
                .toEpochMilli()
        );
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, pastDate, code)
            .claimedValue(new ClaimedValue(baseValue, pastDate))
            .currentValue(baseValue)
            .build();
        assertFalse(point.isUsable());
    }

    @Test
    void givenZeroCurrentValue_isUsable_shouldReturnFalse() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code)
            .claimedValue(new ClaimedValue(baseValue, createdAt))
            .currentValue(0)
            .build();
        assertFalse(point.isUsable());
    }
}