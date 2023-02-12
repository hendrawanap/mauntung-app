package com.mauntung.mauntung.domain.model.point;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PointFactoryImplTest {
    static PointFactory pointFactory;
    static int baseValue;
    static int claimableDuration;
    static int usableDuration;
    static Date createdAt;
    static UUID code;

    @BeforeAll
    static void beforeAll() {
        pointFactory = new PointFactoryImpl();
        baseValue = 10;
        claimableDuration = 10;
        usableDuration = 10;
        createdAt = new Date();
        code = UUID.randomUUID();
    }

    @Test
    void givenCompleteArgs_build_shouldNotThrow() {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertDoesNotThrow(pointBuilder::build);
    }

    @Test
    void givenNullClaimedValueAndNonNullCurrentValue_build_shouldThrow() {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.currentValue(baseValue);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @Test
    void givenNullCurrentValueAndNonNullClaimedValue_build_shouldThrow() {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.claimedValue(new ClaimedValue(baseValue, createdAt));
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneBaseValue_build_shouldThrow(int baseValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneBaseClaimableDuration_build_shouldThrow(int claimableDuration) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneUsableDuration_build_shouldThrow(int usableDuration) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneClaimedValue_build_shouldThrow(int claimedValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.claimedValue(new ClaimedValue(claimedValue, createdAt));
        pointBuilder.currentValue(claimedValue);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersProvider")
    void givenNegativeCurrentValue_build_shouldThrow(int currentValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.claimedValue(new ClaimedValue(baseValue, createdAt));
        pointBuilder.currentValue(currentValue);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersProvider")
    void givenZeroOrPositiveCurrentValue_build_shouldNotThrow(int currentValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.claimedValue(new ClaimedValue(baseValue, createdAt));
        pointBuilder.currentValue(currentValue);
        assertDoesNotThrow(pointBuilder::build);
    }

    static IntStream lessThanOneIntegersProvider() {
        return IntStream.range(-10, 1);
    }

    static IntStream negativeIntegersProvider() {
        return IntStream.range(-10, 0);
    }

    static IntStream zeroOrPositiveIntegersProvider() {
        return IntStream.range(0, 10);
    }
}