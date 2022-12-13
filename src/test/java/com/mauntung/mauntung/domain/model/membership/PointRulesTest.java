package com.mauntung.mauntung.domain.model.membership;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PointRulesTest {
    static Integer redeemTtl = 1;
    static Integer pointClaimableDuration = 1;
    static Integer pointUsableDuration = 1;
    static String distributionMethod = PointRules.DistributionMethod.POINT_CODE_GENERATION;
    static Set<String> rewardClaimMethods = Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER);
    static PointGeneration pointGeneration = new PointGeneration(PointGeneration.TYPE_NOMINAL, 10, 10_000);
    static Set<String> invalidRewardClaimMethods = Set.of("invalid", PointRules.RewardClaimMethod.BY_CUSTOMER);

    static Stream<Arguments> invalidArgsProvider() {
        return Stream.of(
            Arguments.arguments(-1, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration),
            Arguments.arguments(redeemTtl, -1, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration),
            Arguments.arguments(redeemTtl, pointClaimableDuration, -1, distributionMethod, rewardClaimMethods, pointGeneration),
            Arguments.arguments(redeemTtl, pointClaimableDuration, pointUsableDuration, "invalid", rewardClaimMethods, pointGeneration),
            Arguments.arguments(redeemTtl, pointClaimableDuration, pointUsableDuration, distributionMethod, invalidRewardClaimMethods, pointGeneration)
        );
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("invalidArgsProvider")
    void givenInvalidArgs_constructor_shouldThrowsException(Integer redeemTtl, Integer pointClaimableDuration, Integer pointUsableDuration, String distributionMethod, Set<String> rewardClaimMethods, PointGeneration pointGeneration) {
        assertThrows(IllegalArgumentException.class, () -> new PointRules(redeemTtl, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration));
    }

    @Test
    void givenValidArgs_constructor_shouldNotThrowsException() {
        assertDoesNotThrow(() -> new PointRules(redeemTtl, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration));
    }
}