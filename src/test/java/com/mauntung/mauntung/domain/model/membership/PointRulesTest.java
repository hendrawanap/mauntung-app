package com.mauntung.mauntung.domain.model.membership;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    static PointRules.DistributionMethod distributionMethod = PointRules.DistributionMethod.POINT_CODE_GENERATION;
    static Set<PointRules.RewardClaimMethod> rewardClaimMethods = Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER);
    static PointGeneration pointGeneration = new PointGeneration(PointGeneration.Type.NOMINAL, 10, 10_000);
    static ObjectMapper jsonMapper = new ObjectMapper();

    static Stream<Arguments> invalidArgsProvider() {
        return Stream.of(
            Arguments.arguments(-1, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration),
            Arguments.arguments(redeemTtl, -1, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration),
            Arguments.arguments(redeemTtl, pointClaimableDuration, -1, distributionMethod, rewardClaimMethods, pointGeneration)
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
    void givenInvalidArgs_constructor_shouldThrowsException(Integer redeemTtl, Integer pointClaimableDuration, Integer pointUsableDuration, PointRules.DistributionMethod distributionMethod, Set<PointRules.RewardClaimMethod> rewardClaimMethods, PointGeneration pointGeneration) {
        assertThrows(IllegalArgumentException.class, () -> new PointRules(redeemTtl, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration));
    }

    @Test
    void givenValidArgs_constructor_shouldNotThrowsException() {
        assertDoesNotThrow(() -> new PointRules(redeemTtl, pointClaimableDuration, pointUsableDuration, distributionMethod, rewardClaimMethods, pointGeneration));
    }

    @Test
    void distributionMethodEnum_fromStringLabel_shouldNotThrowsException() {
        assertDoesNotThrow(() -> PointRules.DistributionMethod.fromStringLabel("generate"));
        assertDoesNotThrow(() -> PointRules.DistributionMethod.fromStringLabel("scan"));
    }

    @Test
    void distributionMethodEnum_toString_shouldReturnUppercaseLabel() {
        assertEquals("GENERATE", PointRules.DistributionMethod.POINT_CODE_GENERATION.toString());
        assertEquals("SCAN", PointRules.DistributionMethod.CUSTOMER_CODE_SCANNING.toString());
    }

    @Test
    void whenWriteValueAsString_distributionMethodEnum_shouldReturnToString() throws JsonProcessingException {
        assertEquals(String.format("\"%s\"", PointRules.DistributionMethod.POINT_CODE_GENERATION), jsonMapper.writeValueAsString(PointRules.DistributionMethod.POINT_CODE_GENERATION));
        assertEquals(String.format("\"%s\"", PointRules.DistributionMethod.CUSTOMER_CODE_SCANNING), jsonMapper.writeValueAsString(PointRules.DistributionMethod.CUSTOMER_CODE_SCANNING));
    }

    @Test
    void rewardClaimMethodEnum_fromStringLabel_shouldNotThrowsException() {
        assertDoesNotThrow(() -> PointRules.RewardClaimMethod.fromStringLabel("customer"));
        assertDoesNotThrow(() -> PointRules.RewardClaimMethod.fromStringLabel("merchant"));
    }

    @Test
    void rewardClaimMethodEnum_toString_shouldReturnUppercaseLabel() {
        assertEquals("CUSTOMER", PointRules.RewardClaimMethod.BY_CUSTOMER.toString());
        assertEquals("MERCHANT", PointRules.RewardClaimMethod.BY_MERCHANT.toString());
    }

    @Test
    void whenWriteValueAsString_rewardClaimMethodEnum_shouldReturnToString() throws JsonProcessingException {
        assertEquals(String.format("\"%s\"", PointRules.RewardClaimMethod.BY_CUSTOMER), jsonMapper.writeValueAsString(PointRules.RewardClaimMethod.BY_CUSTOMER));
        assertEquals(String.format("\"%s\"", PointRules.RewardClaimMethod.BY_MERCHANT), jsonMapper.writeValueAsString(PointRules.RewardClaimMethod.BY_MERCHANT));
    }
}