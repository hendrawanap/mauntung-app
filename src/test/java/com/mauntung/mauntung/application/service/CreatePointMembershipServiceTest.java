package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.MerchantNotFoundException;
import com.mauntung.mauntung.application.exception.RewardNotFoundException;
import com.mauntung.mauntung.application.exception.TierNotFoundException;
import com.mauntung.mauntung.application.port.membership.CreatePointMembershipCommand;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.membership.PointRules;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePointMembershipServiceTest {
    private static MerchantRepository merchantRepository;
    private static RewardRepository rewardRepository;
    private static TierRepository tierRepository;
    private static MembershipRepository membershipRepository;
    private static Long userId;
    private static PointRules pointRules;

    @BeforeAll
    static void beforeAll() {
        merchantRepository = mock(MerchantRepository.class);
        rewardRepository = mock(RewardRepository.class);
        tierRepository = mock(TierRepository.class);
        membershipRepository = mock(MembershipRepository.class);
        userId = 1L;
        pointRules = mock(PointRules.class);
    }
    @Test
    void givenNonExistingMerchantUserId_apply_shouldThrowsException() {
        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, Set.of(), new Date(), pointRules, Set.of());
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);

        assertThrows(MerchantNotFoundException.class, () -> service.apply(command));
    }

    @Test
    void givenNonExistingRewardId_apply_shouldThrowsException() {
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class)
        );

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, rewardIds, new Date(), pointRules, Set.of());
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);

        assertThrows(RewardNotFoundException.class, () -> service.apply(command));
    }

    @Test
    void givenNonExistingTierId_apply_shouldThrowsException() {
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        Set<Long> tierIds = Set.of(1L, 2L, 3L);
        Set<Tier> tiers = Set.of(
            mock(Tier.class),
            mock(Tier.class)
        );

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(tierRepository.findAllById(tierIds)).thenReturn(tiers);

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, rewardIds, new Date(), pointRules, tierIds);
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);

        assertThrows(TierNotFoundException.class, () -> service.apply(command));
    }

    @Test
    void givenMembershipRepositoryReturnEmpty_apply_shouldThrowsException() {
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        Set<Long> tierIds = Set.of(1L, 2L, 3L);
        Tier tier1 = mock(Tier.class);
        Tier tier2 = mock(Tier.class);
        Tier tier3 = mock(Tier.class);
        Set<Tier> tiers = Set.of(
            tier1,
            tier2,
            tier3
        );

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(tierRepository.findAllById(tierIds)).thenReturn(tiers);
        when(membershipRepository.save(mock(Membership.class))).thenReturn(Optional.empty());

        when(tier1.getRequiredPoints()).thenReturn(0);
        when(tier2.getRequiredPoints()).thenReturn(100);
        when(tier3.getRequiredPoints()).thenReturn(200);

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, rewardIds, new Date(), pointRules, tierIds);
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);

        assertThrows(RuntimeException.class, () -> service.apply(command));
    }

    @Test
    void givenNullTierIdsAndMembershipRepositoryReturnLong_apply_shouldReturnResponse() {
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        long membershipId = 1;

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(membershipRepository.save(any())).thenReturn(Optional.of(membershipId));

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, rewardIds, new Date(), pointRules);
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);

        assertNotNull(service.apply(command));
    }

    @Test
    void givenNonNullTierIdsAndMembershipRepositoryReturnLong_apply_shouldReturnResponse() {
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        Set<Long> tierIds = Set.of(1L, 2L, 3L);
        Tier tier1 = mock(Tier.class);
        Tier tier2 = mock(Tier.class);
        Tier tier3 = mock(Tier.class);
        Set<Tier> tiers = Set.of(
            tier1,
            tier2,
            tier3
        );

        long membershipId = 1;

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(tierRepository.findAllById(tierIds)).thenReturn(tiers);
        when(membershipRepository.save(any())).thenReturn(Optional.of(membershipId));

        when(tier1.getRequiredPoints()).thenReturn(0);
        when(tier2.getRequiredPoints()).thenReturn(100);
        when(tier3.getRequiredPoints()).thenReturn(200);

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, rewardIds, new Date(), pointRules, tierIds);
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);

        assertNotNull(service.apply(command));
    }
}