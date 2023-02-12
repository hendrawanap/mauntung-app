package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TierFactoryImplTest {
    static TierFactory tierFactory;
    static Long id;
    static String name;
    static Set<Reward> rewards;
    static int requiredPoints;
    static float multiplierFactor;

    @BeforeAll
    static void beforeAll() {
        tierFactory = new TierFactoryImpl();
        id = 1L;
        name = "name";
        rewards = Set.of();
        requiredPoints = 10;
        multiplierFactor = 1F;
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeRequiredPoints_build_shouldThrow(int wrongRequiredPoints) {
        assertThrows(IllegalArgumentException.class, () -> tierFactory.builder(name, rewards, wrongRequiredPoints)
            .multiplierFactor(multiplierFactor)
            .id(id)
            .build()
        );
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveRequiredPoints_build_shouldNotThrow(int correctRequiredPoints) {
        assertDoesNotThrow(() -> tierFactory.builder(name, rewards, correctRequiredPoints)
            .multiplierFactor(multiplierFactor)
            .id(id)
            .build()
        );
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeMultiplierFactor_build_shouldThrow(float wrongMultiplierFactor) {
        assertThrows(IllegalArgumentException.class, () -> tierFactory.builder(name, rewards, requiredPoints)
            .multiplierFactor(wrongMultiplierFactor)
            .id(id)
            .build()
        );
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveMultiplierFactor_build_shouldNotThrow(int correctMultiplierFactor) {
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