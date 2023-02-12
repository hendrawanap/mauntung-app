package com.mauntung.mauntung.domain.model.membership;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class StampRulesTest {
    static final int redeemTtl = 10;
    static final int usableDuration = 10;
    static final int cardCapacity = 10;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("positiveIntegersStream")
    void givenZeroOrPositiveRedeemTtl_constructor_shouldNotThrow(int redeemTtl) {
        assertDoesNotThrow(() -> new StampRules(redeemTtl, usableDuration, cardCapacity));
    }

    @ParameterizedTest
    @MethodSource("positiveIntegersStream")
    void givenZeroOrPositiveUsableDuration_constructor_shouldNotThrow(int usableDuration) {
        assertDoesNotThrow(() -> new StampRules(redeemTtl, usableDuration, cardCapacity));
    }

    @ParameterizedTest
    @MethodSource("validCardCapacitiesProvider")
    void givenValidCardCapacity_constructor_shouldNotThrow(int cardCapacity) {
        assertDoesNotThrow(() -> new StampRules(redeemTtl, usableDuration, cardCapacity));
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneCardCapacity_constructor_shouldThrow(int cardCapacity) {
        assertThrows(IllegalArgumentException.class, () -> new StampRules(redeemTtl, usableDuration, cardCapacity));
    }

    @ParameterizedTest
    @MethodSource("moreThanTwentyIntegersProvider")
    void givenMoreThanTwentyCardCapacity_constructor_shouldThrow(int cardCapacity) {
        assertThrows(IllegalArgumentException.class, () -> new StampRules(redeemTtl, usableDuration, cardCapacity));
    }

    static IntStream positiveIntegersStream() {
        return IntStream.range(1, 10);
    }

    static IntStream lessThanOneIntegersProvider() {
        return IntStream.range(-10, 1);
    }

    static IntStream moreThanTwentyIntegersProvider() {
        return IntStream.range(21, 30);
    }

    static IntStream validCardCapacitiesProvider() {
        return IntStream.range(1, 21);
    }
}