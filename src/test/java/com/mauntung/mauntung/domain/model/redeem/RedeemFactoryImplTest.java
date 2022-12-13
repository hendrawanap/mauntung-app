package com.mauntung.mauntung.domain.model.redeem;

import com.mauntung.mauntung.domain.model.reward.Reward;
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
    static RedeemFactory redeemFactory = new RedeemFactoryImpl();
    static String name = "name";
    static String description = "desc";
    static String termsCondition = "terms";
    static int cost = 10;
    static UUID code = UUID.randomUUID();
    static Reward reward = mock(Reward.class);
    static Instant now = Instant.now();
    static Date createdAt = new Date();
    static Date expiredAt = new Date(now.plus(10, ChronoUnit.DAYS).toEpochMilli());

    static IntStream negativeIntegersStream() {
        return IntStream.range(-10, 0);
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeCost_build_shouldThrowsException(int cost) {
        RedeemBuilder redeemBuilder = redeemFactory.builder(name, description, termsCondition, cost, code, reward, createdAt, expiredAt);
        assertThrows(IllegalArgumentException.class, redeemBuilder::build);
    }

    @Test
    void givenCompleteArgs_build_shouldNotThrowsException() {
        RedeemBuilder redeemBuilder = redeemFactory.builder(name, description, termsCondition, cost, code, reward, createdAt, expiredAt);
        assertDoesNotThrow(redeemBuilder::build);
    }
}