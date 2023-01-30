package com.mauntung.mauntung.domain.model.stamp;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StampTest {
    @Test
    void givenPassedExpiredAtAndNullUsedAt_isUsable_shouldReturnFalse() {
        Date now = new Date();
        Date expiredAt = createPastDate(1, now);
        Date createdAt = createPastDate(2, now);

        Stamp stamp = new Stamp(expiredAt, createdAt);
        boolean isUsable = stamp.isUsable();

        assertFalse(isUsable);
    }

    @Test
    void givenFutureExpiredAtAndNullUsedAt_isUsable_shouldReturnTrue() {
        Date now = new Date();
        Date expiredAt = createFutureDate(1, now);
        Date createdAt = createPastDate(1, now);

        Stamp stamp = new Stamp(expiredAt, createdAt);
        boolean isUsable = stamp.isUsable();

        assertTrue(isUsable);
    }

    @Test
    void givenFutureExpiredAtAndNonNullUsedAt_isUsable_shouldReturnFalse() {
        Date now = new Date();
        Date expiredAt = createFutureDate(1, now);
        Date createdAt = createPastDate(2, now);
        Date usedAt = createPastDate(1, now);

        Stamp stamp = new Stamp(expiredAt, createdAt, usedAt);
        boolean isUsable = stamp.isUsable();

        assertFalse(isUsable);
    }

    @Test
    void givenNullUsedAt_isUsed_shouldReturnFalse() {
        Date now = new Date();
        Date expiredAt = createFutureDate(1, now);
        Date createdAt = createPastDate(1, now);

        Stamp stamp = new Stamp(expiredAt, createdAt);
        boolean isUsed = stamp.isUsed();

        assertFalse(isUsed);
    }

    @Test
    void givenNonNullUsedAt_isUsed_shouldReturnTrue() {
        Date now = new Date();
        Date expiredAt = createFutureDate(1, now);
        Date createdAt = createPastDate(2, now);
        Date usedAt = createPastDate(1, now);

        Stamp stamp = new Stamp(expiredAt, createdAt, usedAt);
        boolean isUsed = stamp.isUsed();

        assertTrue(isUsed);
    }

    Date createPastDate(int daysPassed, Date dateSince) {
        return new Date(dateSince.toInstant().minus(daysPassed, ChronoUnit.DAYS).toEpochMilli());
    }

    Date createFutureDate(int daysInFuture, Date dateSince) {
        return new Date(dateSince.toInstant().plus(daysInFuture, ChronoUnit.DAYS).toEpochMilli());
    }
}