package com.mauntung.mauntung.domain.model.reward;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RewardTest {
    static final RewardFactory rewardFactory = new RewardFactoryImpl();
    static final String name = "name";
    static final String description = "description";
    static final String termsCondition = "termsCondition";
    static final int cost = 10;
    static final int stock = 10;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
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
        Instant now = Instant.now();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .startPeriod(new Date(now.minus(1, ChronoUnit.DAYS).toEpochMilli()))
            .endPeriod(new Date(now.plus(10, ChronoUnit.DAYS).toEpochMilli()))
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
        Instant now = Instant.now();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .startPeriod(new Date(now.toEpochMilli()))
            .endPeriod(new Date(now.plus(10, ChronoUnit.DAYS).toEpochMilli()))
            .build();
        assertTrue(reward.isClaimable());
    }

    @Test
    void givenStartPeriodAndEndPeriodAreAfterNow_isClaimable_shouldReturnFalse() {
        Instant now = Instant.now();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .startPeriod(new Date(now.plus(1, ChronoUnit.DAYS).toEpochMilli()))
            .endPeriod(new Date(now.plus(10, ChronoUnit.DAYS).toEpochMilli()))
            .build();
        assertFalse(reward.isClaimable());
    }

    @Test
    void givenStartPeriodIsBeforeNowAndEndPeriodIsNow_isClaimable_shouldReturnFalse() {
        Instant now = Instant.now();
        Reward reward = rewardFactory.builder(name, description, termsCondition, cost)
            .stock(stock)
            .startPeriod(new Date(now.minus(10, ChronoUnit.DAYS).toEpochMilli()))
            .endPeriod(new Date(now.toEpochMilli()))
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
}