package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.membership.CreateStampMembershipCommand;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipResponse;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateStampMembershipServiceTest {
    @Test
    void givenNonExistingMerchantUserId_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CreateStampMembershipCommand command = new CreateStampMembershipCommand("name", userId, Set.of(), 10, 10, 10);
        CreateStampMembershipService service = new CreateStampMembershipService(merchantRepository, rewardRepository, membershipRepository);
        CreateStampMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenNonExistingRewardId_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class)
        );

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);

        CreateStampMembershipCommand command = new CreateStampMembershipCommand("name", userId, rewardIds, 10, 10, 10);
        CreateStampMembershipService service = new CreateStampMembershipService(merchantRepository, rewardRepository, membershipRepository);
        CreateStampMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenMembershipRepositoryReturnEmpty_apply_shouldReturnErrorResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
        Set<Long> rewardIds = Set.of(1L, 2L, 3L);
        Set<Reward> rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.of(mock(Merchant.class)));
        when(rewardRepository.findAllById(rewardIds)).thenReturn(rewards);
        when(membershipRepository.save(any())).thenReturn(Optional.empty());

        CreateStampMembershipCommand command = new CreateStampMembershipCommand("name", userId, rewardIds, 10, 10, 10);
        CreateStampMembershipService service = new CreateStampMembershipService(merchantRepository, rewardRepository, membershipRepository);
        CreateStampMembershipResponse response = service.apply(command);

        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenMembershipRepositoryReturnLong_apply_shouldReturnSuccessResponse() {
        MerchantRepository merchantRepository = mock(MerchantRepository.class);
        RewardRepository rewardRepository = mock(RewardRepository.class);
        MembershipRepository membershipRepository = mock(MembershipRepository.class);

        long userId = 1;
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

        CreateStampMembershipCommand command = new CreateStampMembershipCommand("name", userId, rewardIds, 10, 10, 10);
        CreateStampMembershipService service = new CreateStampMembershipService(merchantRepository, rewardRepository, membershipRepository);
        CreateStampMembershipResponse response = service.apply(command);

        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }
}