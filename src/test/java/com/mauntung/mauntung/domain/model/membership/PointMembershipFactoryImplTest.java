package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
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
    static final PointMembershipFactory membershipFactory = new PointMembershipFactoryImpl();
    static final RewardFactory rewardFactory = new RewardFactoryImpl();
    static final String name = "name";
    static final Set<Reward> rewards = Set.of(rewardFactory.builder("name", "description", "terms", 10).build());
    static final Date createdAt = new Date();
    static final PointRules pointRules = new PointRules(
        10,
        10,
        10,
        PointRules.DistributionMethod.POINT_CODE_GENERATION,
        Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER),
        new PointGeneration(PointGeneration.Type.NOMINAL, 10, 10_000)
    );
    static Set<Tier> tiers;

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

    @BeforeAll
    static void beforeAll() {
        Tier firstTier = mock(Tier.class);
        Tier secondTier = mock(Tier.class);

        when(firstTier.getRequiredPoints()).thenReturn(0);
        when(secondTier.getRequiredPoints()).thenReturn(10);

        tiers = Set.of(firstTier, secondTier);
    }

    @Test
    void givenValidTiers_build_shouldNotThrowsException() {
        PointMembershipBuilder builder = membershipFactory.builder(name, rewards, createdAt, pointRules, false)
            .tiers(tiers);
        assertDoesNotThrow(builder::build);
    }

    @ParameterizedTest
    @MethodSource("invalidTiersProvider")
    void givenInvalidTiers_build_shouldThrowsException(Set<Tier> tiers) {
        PointMembershipBuilder builder = membershipFactory.builder(name, rewards, createdAt, pointRules, false)
            .tiers(tiers);
        assertThrows(IllegalArgumentException.class, builder::build);
    }
}