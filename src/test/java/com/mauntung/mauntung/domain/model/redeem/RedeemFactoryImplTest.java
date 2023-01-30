package com.mauntung.mauntung.domain.model.redeem;

import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedeemFactoryImplTest {
    static RedeemFactory redeemFactory;
    static String name;
    static String description;
    static String termsCondition;
    static int cost;
    static UUID code;
    static Reward reward;
    static Instant now;
    static Date createdAt;
    static Date expiredAt;

    @BeforeAll
    static void beforeAll() {
        redeemFactory = new RedeemFactoryImpl();
        name = "name";
        description = "desc";
        termsCondition = "terms";
        cost = 10;
        code = UUID.randomUUID();
        reward = mock(Reward.class);
        now = Instant.now();
        createdAt = new Date();
        expiredAt = new Date(now.plus(10, ChronoUnit.DAYS).toEpochMilli());
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeCost_build_shouldThrowsException(int cost) {
        RedeemBuilder redeemBuilder = redeemFactory.builder(name, description, termsCondition, cost, code, reward, createdAt, expiredAt);
        assertThrows(IllegalArgumentException.class, redeemBuilder::build);
    }

    @Test
    void givenCompleteArgs_build_shouldNotThrowException() {
        RedeemBuilder redeemBuilder = redeemFactory.builder(name, description, termsCondition, cost, code, reward, createdAt, expiredAt);
        assertDoesNotThrow(redeemBuilder::build);
    }

    static IntStream negativeIntegersStream() {
        return IntStream.range(-10, 0);
    }
}