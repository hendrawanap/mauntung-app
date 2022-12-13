package com.mauntung.mauntung.domain.model.point;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PointFactoryImplTest {
    static final PointFactory pointFactory = new PointFactoryImpl();
    static final int baseValue = 10;
    static final int claimableDuration = 10;
    static final int usableDuration = 10;
    static final Date createdAt = new Date();
    static final UUID code = UUID.randomUUID();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenCompleteArgs_build_shouldNotThrowsException() {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertDoesNotThrow(pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneBaseValue_build_shouldThrowsException(int baseValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneBaseClaimableDuration_build_shouldThrowsException(int claimableDuration) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneUsableDuration_build_shouldThrowsException(int usableDuration) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneClaimedValue_build_shouldThrowsException(int claimedValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.claimedValue(claimedValue);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersProvider")
    void givenNegativeCurrentValue_build_shouldThrowsException(int currentValue) {
        PointBuilder pointBuilder = pointFactory.builder(baseValue, claimableDuration, usableDuration, createdAt, code);
        pointBuilder.currentValue(currentValue);
        assertThrows(IllegalArgumentException.class, pointBuilder::build);
    }

    static IntStream lessThanOneIntegersProvider() {
        return IntStream.range(-10, 1);
    }

    static IntStream negativeIntegersProvider() {
        return IntStream.range(-10, 0);
    }
}