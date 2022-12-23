package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TierFactoryImplTest {
    static final RewardFactory rewardFactory = new RewardFactoryImpl();
    static final TierFactory tierFactory = new TierFactoryImpl();

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
    void givenNegativeRequiredPoints_build_shouldThrowsException(int wrongRequiredPoints) {
        assertThrows(IllegalArgumentException.class, () -> tierFactory.builder(name, rewards, wrongRequiredPoints)
            .multiplierFactor(multiplierFactor)
            .id(id)
            .build()
        );
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveRequiredPoints_build_shouldNotThrowsException(int correctRequiredPoints) {
        assertDoesNotThrow(() -> tierFactory.builder(name, rewards, correctRequiredPoints)
            .multiplierFactor(multiplierFactor)
            .id(id)
            .build()
        );
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeMultiplierFactor_build_shouldThrowsException(float wrongMultiplierFactor) {
        assertThrows(IllegalArgumentException.class, () -> tierFactory.builder(name, rewards, requiredPoints)
            .multiplierFactor(wrongMultiplierFactor)
            .id(id)
            .build()
        );
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveMultiplierFactor_build_shouldNotThrowsException(int correctMultiplierFactor) {
        assertDoesNotThrow(() -> tierFactory.builder(name, rewards, requiredPoints)
            .multiplierFactor((float) correctMultiplierFactor)
            .id(id)
            .build()
        );
    }

    @Test
    void givenNullMultiplierFactor_build_shouldReturnTierWithDefaultMultiplierFactor() {
        Tier tier = tierFactory.builder(name, rewards, requiredPoints)
            .id(id)
            .build();
        assertEquals(Tier.DEFAULT_MULTIPLIER_FACTOR, tier.getMultiplierFactor());
    }

    static IntStream zeroOrPositiveIntegersStream() {
        return IntStream.range(0, 10);
    }

    static IntStream negativeIntegersStream() {
        return IntStream.range(-10, 0);
    }
}