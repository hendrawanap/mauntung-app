package com.mauntung.mauntung.domain.model.membership;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PointGenerationTest {
    static PointGeneration.Type type;
    static int points;
    static int divider;

    @BeforeAll
    static void beforeAll() {
        type = PointGeneration.Type.NOMINAL;
        points = 10;
        divider = 10_000;
    }

    @Test
    void givenCompleteArgs_constructor_shouldNotThrow() {
        assertDoesNotThrow(() -> new PointGeneration(type, points, divider));
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOnePoint_constructor_shouldThrow(int points) {
        assertThrows(IllegalArgumentException.class, () -> new PointGeneration(type, points, divider));
    }

    @ParameterizedTest
    @MethodSource("lessThanOneIntegersProvider")
    void givenLessThanOneDivider_constructor_shouldThrow(int divider) {
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