package com.mauntung.mauntung.domain.model.reward;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {
    static RewardFactory rewardFactory;
    static String name;
    static String description;
    static String termsCondition;
    static int cost;
    static int stock;

    @BeforeAll
    static void beforeAll() {
        rewardFactory = new RewardFactoryImpl();
        name = "name";
        description = "description";
        termsCondition = "termsCondition";
        cost = 10;
        stock = 10;
    }

    @Test
    void givenNullPeriodsAndNonNullStock_isClaimable_shouldReturnTrue() {
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .build();

        assertTrue(reward.isClaimable());
    }

    @Test
    void givenNullStockAndValidPeriods_isClaimable_shouldReturnTrue() {
        Date now = new Date();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .startPeriod(createPastDate(1, now))
            .endPeriod(createFutureDate(10, now))
            .build();

        assertTrue(reward.isClaimable());
    }

    @Test
    void givenNullPeriodsAndNullStock_isClaimable_shouldReturnTrue() {
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost).build();

        assertTrue(reward.isClaimable());
    }

    @Test
    void givenStartPeriodIsNowAndEndPeriodAfterNow_isClaimable_shouldReturnTrue() {
        Date now = new Date();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .startPeriod(now)
            .endPeriod(createFutureDate(10, now))
            .build();

        assertTrue(reward.isClaimable());
    }

    @Test
    void givenStartPeriodAndEndPeriodAreAfterNow_isClaimable_shouldReturnFalse() {
        Date now = new Date();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .startPeriod(createFutureDate(1, now))
            .endPeriod(createFutureDate(10, now))
            .build();

        assertFalse(reward.isClaimable());
    }

    @Test
    void givenStartPeriodIsBeforeNowAndEndPeriodIsNow_isClaimable_shouldReturnFalse() {
        Date now = new Date();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .startPeriod(createPastDate(10, now))
            .endPeriod(now)
            .build();

        assertFalse(reward.isClaimable());
    }

    @Test
    void givenZeroStock_isClaimable_shouldReturnFalse() {
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(0)
            .build();

        assertFalse(reward.isClaimable());
    }

    Date createPastDate(int daysPassed, Date dateSince) {
        return new Date(dateSince.toInstant().minus(daysPassed, ChronoUnit.DAYS).toEpochMilli());
    }

    Date createFutureDate(int daysInFuture, Date dateSince) {
        return new Date(dateSince.toInstant().plus(daysInFuture, ChronoUnit.DAYS).toEpochMilli());
    }
}