package com.mauntung.mauntung.domain.model.membership;

import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactory;
import com.mauntung.mauntung.domain.model.merchant.MerchantFactoryImpl;
import com.mauntung.mauntung.domain.model.reward.Reward;
import com.mauntung.mauntung.domain.model.reward.RewardFactory;
import com.mauntung.mauntung.domain.model.reward.RewardFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PointMembershipFactoryImplTest {
    static final PointMembershipFactory membershipFactory = new PointMembershipFactoryImpl();
    static final RewardFactory rewardFactory = new RewardFactoryImpl();
    static final MerchantFactory merchantFactory = new MerchantFactoryImpl();
    static final String name = "name";
    static final Merchant merchant = merchantFactory.builder("name", new Date()).build();
    static final Set<Reward> rewards = Set.of(rewardFactory.builder("name", "description", "terms", 10).build());
    static final Date createdAt = new Date();
    static final PointRules pointRules = new PointRules(
        10,
        10,
        10,
        PointRules.DistributionMethod.POINT_CODE_GENERATION,
        Set.of(PointRules.RewardClaimMethod.BY_CUSTOMER),
        new PointGeneration(PointGeneration.TYPE_NOMINAL, 10, 10_000)
    );
    static final Set<Tier> tiers = Set.of(
        Tier.withoutId(
            "bronze",
            Set.of(rewardFactory.builder("", "", "", 10).build()),
            0,
            1F
        ),
        Tier.withoutId(
            "silver",
            Set.of(rewardFactory.builder("", "", "", 10).build()),
            10,
            1.1F
        )
    );

    static Stream<Set<Tier>> invalidTiersProvider() {
        return Stream.of(
            Set.of(),
            Set.of(
                Tier.withoutId(
                    "bronze",
                    Set.of(rewardFactory.builder("", "", "", 10).build()),
                    0,
                    1F
                )
            ),
            Set.of(
                Tier.withoutId(
                    "bronze",
                    Set.of(rewardFactory.builder("", "", "", 10).build()),
                    10,
                    1F
                ),
                Tier.withoutId(
                    "silver",
                    Set.of(rewardFactory.builder("", "", "", 10).build()),
                    20,
                    1.1F
                )
            ),
            Set.of(
                Tier.withoutId(
                    "bronze",
                    Set.of(rewardFactory.builder("", "", "", 10).build()),
                    0,
                    1F
                ),
                Tier.withoutId(
                    "silver",
                    Set.of(rewardFactory.builder("", "", "", 10).build()),
                    20,
                    1.1F
                ),
                Tier.withoutId(
                    "Gold",
                    Set.of(rewardFactory.builder("", "", "", 10).build()),
                    20,
                    1.1F
                )
            )
        );
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenValidTiers_build_shouldNotThrowsException() {
        PointMembershipBuilder builder = membershipFactory.builder(name, merchant, rewards, createdAt, pointRules)
            .tiers(tiers);
        assertDoesNotThrow(builder::build);
    }

    @ParameterizedTest
    @MethodSource("invalidTiersProvider")
    void givenInvalidTiers_build_shouldThrowsException(Set<Tier> tiers) {
        PointMembershipBuilder builder = membershipFactory.builder(name, merchant, rewards, createdAt, pointRules)
            .tiers(tiers);
        assertThrows(IllegalArgumentException.class, builder::build);
    }
}