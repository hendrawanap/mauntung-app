package com.mauntung.mauntung.domain.model.reward;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RewardFactoryImplTest {
    RewardFactory rewardFactory = new RewardFactoryImpl();
    static String name = "name";
    static String description = "description";
    static String termsCondition = "termsCondition";
    static int correctCost = 10;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveCost_build_shouldNotThrowsException(int correctCost) {
        RewardBuilder rewardBuilder = rewardFactory.builder(name, description, termsCondition, correctCost);
        assertDoesNotThrow(rewardBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeCost_build_shouldThrowsException(int wrongCost) {
        RewardBuilder rewardBuilder = rewardFactory.builder(name, description, termsCondition, wrongCost);
        assertThrows(IllegalArgumentException.class, rewardBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("zeroOrPositiveIntegersStream")
    void givenZeroOrPositiveStock_build_shouldNotThrowsException(int correctStock) {
        RewardBuilder rewardBuilder = rewardFactory.builder(name, description, termsCondition, correctCost);
        rewardBuilder.stock(correctStock);
        assertDoesNotThrow(rewardBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("negativeIntegersStream")
    void givenNegativeStock_build_shouldNotThrowsException(int wrongStock) {
        RewardBuilder rewardBuilder = rewardFactory.builder(name, description, termsCondition, correctCost);
        rewardBuilder.stock(wrongStock);
        assertThrows(IllegalArgumentException.class, rewardBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("invalidPeriodArgs")
    void givenInvalidPeriodArgs_build_shouldThrowsException(Date startPeriod, Date endPeriod) {
        RewardBuilder rewardBuilder = rewardFactory.builder(name, description, termsCondition, correctCost);
        rewardBuilder.startPeriod(startPeriod);
        rewardBuilder.endPeriod(endPeriod);
        assertThrows(IllegalArgumentException.class, rewardBuilder::build);
    }

    @ParameterizedTest
    @MethodSource("validPeriodArgs")
    void givenValidPeriodArgs_build_shouldNotThrowException(Date startPeriod, Date endPeriod) {
        RewardBuilder rewardBuilder = rewardFactory.builder(name, description, termsCondition, correctCost);
        rewardBuilder.startPeriod(startPeriod);
        rewardBuilder.endPeriod(endPeriod);
        assertDoesNotThrow(rewardBuilder::build);
    }

    @Test
    void createCopyWithNewStock_shouldReturnNewRewardWithSameAttributesButNewStock() {
        int oldStock = 10;
        int newStock = 1;

        Instant now = Instant.now();
        Reward oldReward = rewardFactory.builder(name, description, termsCondition, correctCost)
            .id(1L)
            .stock(oldStock)
            .startPeriod(new Date(now.minus(1, ChronoUnit.DAYS).toEpochMilli()))
            .endPeriod(new Date(now.plus(10, ChronoUnit.DAYS).toEpochMilli()))
            .build();

        Reward newReward = rewardFactory.createCopyWithNewStock(oldReward, newStock);

        assertEquals(oldReward.getName(), newReward.getName());
        assertEquals(oldReward.getDescription(), newReward.getDescription());
        assertEquals(oldReward.getTermsCondition(), newReward.getTermsCondition());
        assertEquals(oldReward.getCost(), newReward.getCost());
        assertEquals(oldReward.getId(), newReward.getId());
        assertEquals(oldReward.getStartPeriod(), newReward.getStartPeriod());
        assertEquals(oldReward.getEndPeriod(), newReward.getEndPeriod());
        assertEquals(newStock, newReward.getStock());
    }

    static IntStream zeroOrPositiveIntegersStream() {
        return IntStream.range(0, 10);
    }

    static IntStream negativeIntegersStream() {
        return IntStream.range(-10, 0);
    }

    static Stream<Arguments> invalidPeriodArgs() {
        Calendar calendar = new GregorianCalendar();
        List<Arguments> arguments = new ArrayList<>();
        Date startPeriod = null;
        Date endPeriod = null;

        for (int i = 0; i < 5; i++) {
            calendar.set(2022, Calendar.DECEMBER, 20 - i);
            startPeriod = calendar.getTime();

            calendar.set(2022, Calendar.DECEMBER, 19 - i);
            endPeriod = calendar.getTime();

            arguments.add(Arguments.arguments(startPeriod, endPeriod));
        }

        arguments.add(Arguments.arguments(null, endPeriod));
        arguments.add(Arguments.arguments(startPeriod, null));

        return arguments.stream();
    }

    static Stream<Arguments> validPeriodArgs() {
        Calendar calendar = new GregorianCalendar();
        List<Arguments> arguments = new ArrayList<>();
        Date startPeriod;
        Date endPeriod;

        for (int i = 0; i < 5; i++) {
            calendar.set(2022, Calendar.DECEMBER, 19 - i);
            startPeriod = calendar.getTime();

            calendar.set(2022, Calendar.DECEMBER, 20 - i);
            endPeriod = calendar.getTime();

            arguments.add(Arguments.arguments(startPeriod, endPeriod));
        }

        return arguments.stream();
    }
}