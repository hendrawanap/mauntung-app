package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TierTest {
    static final RewardFactory rewardFactory = new RewardFactoryImpl();
    static final Long id = 1L;
    static final String name = "name";
    static final Set<Reward> rewards = Set.of(
        rewardFactory.builder("name", "description", "terms", 10).build()
    );
    static final int requiredPoints = 10;
    static final float multiplierFactor = 1F;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeRequiredPoints_withId_shouldThrowsException(int wrongRequiredPoints) {
        assertThrows(IllegalArgumentException.class, () -> Tier.withId(name, rewards, wrongRequiredPoints, multiplierFactor, id));
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveRequiredPoints_withId_shouldNotThrowsException(int correctRequiredPoints) {
        assertDoesNotThrow(() -> Tier.withId(name, rewards, correctRequiredPoints, multiplierFactor, id));
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeMultiplierFactor_withId_shouldThrowsException(float wrongMultiplierFactor) {
        assertThrows(IllegalArgumentException.class, () -> Tier.withId(name, rewards, requiredPoints, wrongMultiplierFactor, id));
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveMultiplierFactor_withId_shouldNotThrowsException(int correctMultiplierFactor) {
        assertDoesNotThrow(() -> Tier.withId(name, rewards, requiredPoints, correctMultiplierFactor, id));
    }

    static IntStream zeroOrPositiveIntegersStream() {
        return IntStream.range(0, 10);
    }

    static IntStream negativeIntegersStream() {
        return IntStream.range(-10, 0);
    }
}