package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.membership.CreatePointMembershipCommand;
import com.mauntung.mauntung.application.port.membership.CreatePointMembershipResponse;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.application.port.tier.TierRepository;
import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.membership.PointRules;
import com.mauntung.mauntung.domain.model.membership.Tier;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatePointMembershipServiceTest {
    @Test
    void givenNonExistingMerchantUserId_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        PointRules pointRules = mock(PointRules.class);

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, Set.of(), new Date(), pointRules, Set.of());
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);
        CreatePointMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenNonExistingRewardId_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        PointRules pointRules = mock(PointRules.class);

        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class)
        );

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);

        CreatePointMembershipCommand command = new CreatePointMembershipCommand("name", userId, rewardIds, new Date(), pointRules, Set.of());
        CreatePointMembershipService service = new CreatePointMembershipService(merchantRepository, rewardRepository, tierRepository, membershipRepository);
        CreatePointMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenNonExistingTierId_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        PointRules pointRules = mock(PointRules.class);

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
        CreatePointMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenMembershipRepositoryReturnEmpty_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        PointRules pointRules = mock(PointRules.class);

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
        CreatePointMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenNullTierIdsAndMembershipRepositoryReturnLong_apply_shouldReturnSuccessResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        PointRules pointRules = mock(PointRules.class);

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
        CreatePointMembershipResponse response = service.apply(command);

        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }

    @Test
    void givenNonNullTierIdsAndMembershipRepositoryReturnLong_apply_shouldReturnSuccessResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        TierRepository tierRepository = mock(TierRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        PointRules pointRules = mock(PointRules.class);

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
        CreatePointMembershipResponse response = service.apply(command);

        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }
}