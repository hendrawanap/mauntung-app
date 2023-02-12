package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointMembershipFactoryImplTest {
    static PointMembershipFactory membershipFactory;
    static String name;
    static Set<Reward> rewards;
    static Date createdAt;
    static PointRules pointRules;
    static Set<Tier> tiers;

    @BeforeAll
    static void beforeAll() {
        membershipFactory = new PointMembershipFactoryImpl();
        name = "name";
        rewards = Set.of();
        createdAt = new Date();
        pointRules = mock(PointRules.class);

        Tier firstTier = mock(Tier.class);
        Tier secondTier = mock(Tier.class);

        when(firstTier.getRequiredPoints()).thenReturn(0);
        when(secondTier.getRequiredPoints()).thenReturn(10);

        tiers = Set.of(firstTier, secondTier);
    }

    @Test
    void givenValidTiers_build_shouldNotThrow() {
        PointMembershipBuilder builder = membershipFactory.builder(name, rewards, createdAt, pointRules, false)
            .tiers(tiers);

        assertDoesNotThrow(builder::build);
    }

    @ParameterizedTest
    @MethodSource("invalidTiersProvider")
    void givenInvalidTiers_build_shouldThrow(Set<Tier> tiers) {
        PointMembershipBuilder builder = membershipFactory.builder(name, rewards, createdAt, pointRules, false)
            .tiers(tiers);

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    static Stream<Set<Tier>> invalidTiersProvider() {
        Tier zeroRequiredPointsTier = mock(Tier.class);
        Tier tenRequiredPointsTier = mock(Tier.class);
        Tier twentyRequiredPointsTier1 = mock(Tier.class);
        Tier twentyRequiredPointsTier2 = mock(Tier.class);

        when(zeroRequiredPointsTier.getRequiredPoints()).thenReturn(0);
        when(tenRequiredPointsTier.getRequiredPoints()).thenReturn(10);
        when(twentyRequiredPointsTier1.getRequiredPoints()).thenReturn(20);
        when(twentyRequiredPointsTier2.getRequiredPoints()).thenReturn(20);

        return Stream.of(
            Set.of(),
            Set.of(zeroRequiredPointsTier),
            Set.of(tenRequiredPointsTier, twentyRequiredPointsTier1),
            Set.of(zeroRequiredPointsTier, twentyRequiredPointsTier1, twentyRequiredPointsTier2)
        );
    }
}