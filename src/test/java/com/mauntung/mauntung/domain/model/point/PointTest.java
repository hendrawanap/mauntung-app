package com.mauntung.mauntung.domain.model.point;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    static final PointFactory pointFactory = new PointFactoryImpl();
    static final int baseValue = 10;
    static final int claimableDuration = 10;
    static final int usableDuration = 10;
    static final Date createdAt = new Date();
    static final UUID code = UUID.randomUUID();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenNonNullClaimedAt_isClaimed_shouldReturnTrue() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code)
            .claimedAt(createdAt)
            .claimedValue(baseValue)
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
            .claimedAt(createdAt)
            .claimedValue(baseValue)
            .currentValue(baseValue)
            .build();
        assertFalse(point1.isClaimable());
    }

    @Test
    void givenClaimablePeriodHasPassed_isClaimable_shouldReturnFalse() {
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
            .claimedAt(createdAt)
            .claimedValue(baseValue)
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
    void givenUsablePeriodHasPassed_isUsable_shouldReturnFalse() {
        Date pastDate = new Date(
            createdAt.toInstant()
                .minus(usableDuration + 1, ChronoUnit.DAYS)
                .toEpochMilli()
        );
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, pastDate, code)
            .claimedAt(pastDate)
            .claimedValue(baseValue)
            .currentValue(baseValue)
            .build();
        assertFalse(point.isUsable());
    }

    @Test
    void givenZeroCurrentValue_isUsable_shouldReturnFalse() {
        Point point = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code)
            .claimedAt(createdAt)
            .claimedValue(baseValue)
            .currentValue(0)
            .build();
        assertFalse(point.isUsable());
    }
}