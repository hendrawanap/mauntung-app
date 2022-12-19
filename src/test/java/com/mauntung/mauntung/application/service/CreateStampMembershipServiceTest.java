package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.exception.MerchantNotFoundException;
import com.mauntung.mauntung.application.exception.RewardNotFoundException;
import com.mauntung.mauntung.application.port.membership.CreateStampMembershipCommand;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.merchant.MerchantRepository;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import com.mauntung.mauntung.domain.model.merchant.Merchant;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateStampMembershipServiceTest {
    private static MerchantRepository merchantRepository;
    private static RewardRepository rewardRepository;
    private static MembershipRepository membershipRepository;
    private static Long userId;

    @BeforeAll
    static void beforeAll() {
        merchantRepository = mock(MerchantRepository.class);
        rewardRepository = mock(RewardRepository.class);
        membershipRepository = mock(MembershipRepository.class);
        userId = 1L;
    }
    @Test
    void givenNonExistingMerchantUserId_apply_shouldThrowsException() {
        when(merchantRepository.findByUserId(userId)).thenReturn(Optional.empty());

        CreateStampMembershipCommand command = new CreateStampMembershipCommand("name", userId, Set.of(), 10, 10, 10);
        CreateStampMembershipService service = new CreateStampMembershipService(merchantRepository, rewardRepository, membershipRepository);

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

        CreateStampMembershipCommand command = new CreateStampMembershipCommand("name", userId, rewardIds, 10, 10, 10);
        CreateStampMembershipService service = new CreateStampMembershipService(merchantRepository, rewardRepository, membershipRepository);

        assertThrows(RewardNotFoundException.class, () -> service.apply(command));
    }

    @Test
    void givenMembershipRepositoryReturnEmpty_apply_shouldThrowsException() {
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

        assertThrows(RuntimeException.class, () -> service.apply(command));
    }

    @Test
    void givenMembershipRepositoryReturnLong_apply_shouldReturnsResponse() {
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

        assertNotNull(service.apply(command));
    }
}