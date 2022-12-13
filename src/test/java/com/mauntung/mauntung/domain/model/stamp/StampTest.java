package com.mauntung.mauntung.domain.model.stamp;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StampTest {
    @Test
    void givenPassedExpiredAtAndNullUsedAt_isUsable_shouldReturnFalse() {
        Date now = new Date();

        // Expired at 10 days ago
        Date expiredAt = new Date(now.toInstant().minus(10, ChronoUnit.DAYS).toEpochMilli());

        // Created at 12 days ago
        Date createdAt = new Date(now.toInstant().minus(12, ChronoUnit.DAYS).toEpochMilli());

        Stamp stamp = new Stamp(expiredAt, createdAt);
        assertFalse(stamp.isUsable());
    }

    @Test
    void givenFutureExpiredAtAndNullUsedAt_isUsable_shouldReturnTrue() {
        Date now = new Date();

        // Will be expired in 10 days
        Date expiredAt = new Date(now.toInstant().plus(10, ChronoUnit.DAYS).toEpochMilli());

        // Created at 2 days ago
        Date createdAt = new Date(now.toInstant().minus(2, ChronoUnit.DAYS).toEpochMilli());

        Stamp stamp = new Stamp(expiredAt, createdAt);
        assertTrue(stamp.isUsable());
    }

    @Test
    void givenFutureExpiredAtAndNonNullUsedAt_isUsable_shouldReturnFalse() {
        Date now = new Date();

        // Will be expired in 10 days
        Date expiredAt = new Date(now.toInstant().plus(10, ChronoUnit.DAYS).toEpochMilli());

        // Created at 2 days ago
        Date createdAt = new Date(now.toInstant().minus(2, ChronoUnit.DAYS).toEpochMilli());

        // Used at 1 day ago
        Date usedAt = new Date(now.toInstant().minus(1, ChronoUnit.DAYS).toEpochMilli());

        Stamp stamp = new Stamp(expiredAt, createdAt, usedAt);
        assertFalse(stamp.isUsable());
    }

    @Test
    void givenNullUsedAt_isUsed_shouldReturnFalse() {
        Date now = new Date();

        // Will be expired in 10 days
        Date expiredAt = new Date(now.toInstant().plus(10, ChronoUnit.DAYS).toEpochMilli());

        // Created at 2 days ago
        Date createdAt = new Date(now.toInstant().minus(2, ChronoUnit.DAYS).toEpochMilli());

        Stamp stamp = new Stamp(expiredAt, createdAt);
        assertFalse(stamp.isUsed());
    }

    @Test
    void givenNonNullUsedAt_isUsed_shouldReturnTrue() {
        Date now = new Date();

        // Will be expired in 10 days
        Date expiredAt = new Date(now.toInstant().plus(10, ChronoUnit.DAYS).toEpochMilli());

        // Created at 2 days ago
        Date createdAt = new Date(now.toInstant().minus(2, ChronoUnit.DAYS).toEpochMilli());

        // Used at 1 day ago
        Date usedAt = new Date(now.toInstant().minus(1, ChronoUnit.DAYS).toEpochMilli());

        Stamp stamp = new Stamp(expiredAt, createdAt, usedAt);
        assertTrue(stamp.isUsed());
    }
}