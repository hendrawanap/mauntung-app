package com.mauntung.mauntung.domain.model.membership;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PointGenerationTest {
    static final PointGeneration.Type type = PointGeneration.Type.NOMINAL;
    static final int points = 10;
    static final int divider = 10_000;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenCompleteArgs_constructor_shouldNotThrowsException() {
        assertDoesNotThrow(() -> new PointGeneration(type, points, divider));
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOnePoints_constructor_shouldThrowsException(int points) {
        assertThrows(IllegalArgumentException.class, () -> new PointGeneration(type, points, divider));
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneDivider_constructor_shouldThrowsException(int divider) {
        assertThrows(IllegalArgumentException.class, () -> new PointGeneration(type, points, divider));
    }

    @Test
    void typeEnum_toString_shouldReturnName() {
        assertEquals("NOMINAL", PointGeneration.Type.NOMINAL.toString());
        assertEquals("ITEM", PointGeneration.Type.ITEM.toString());
        assertEquals("FIXED", PointGeneration.Type.FIXED.toString());
    }

    static IntStream lessThanOneIntegersProvider() {
        return IntStream.range(-10, 1);
    }
}