package com.mauntung.mauntung.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.application.exception.MembershipNotFoundException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsQuery;
import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsResponse;
import com.mauntung.mauntung.application.port.reward.ListMembershipRewardsUseCase;
import com.mauntung.mauntung.domain.model.membership.Membership;
import com.mauntung.mauntung.domain.model.reward.Reward;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListMembershipRewardsServiceTest {
    private static MembershipRepository membershipRepository;
    private static Set<Reward> rewards;
    private static final Long existingUserId = 1L;
    private static final Long nonExistingUserId = 2L;

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        Membership membership = mock(Membership.class);
        membershipRepository = mock(MembershipRepository.class);
        rewards = Set.of(
            mock(Reward.class),
            mock(Reward.class),
            mock(Reward.class)
        );

        when(membershipRepository.findByUserId(existingUserId)).thenReturn(Optional.of(membership));
        when(membershipRepository.findByUserId(nonExistingUserId)).thenReturn(Optional.empty());
        when(membership.getRewards()).thenReturn(rewards);
    }

    @Test
    void whenGivenExistingUserId_apply_shouldReturnResponseWithCorrectRewardsQty() {
        ListMembershipRewardsQuery query = ListMembershipRewardsQuery.builder(existingUserId).build();
        ListMembershipRewardsUseCase service = new ListMembershipRewardsService(membershipRepository);
        ListMembershipRewardsResponse response = service.apply(query);
        assertEquals(rewards.size(), response.getRewards().size());
    }

    @Test
    void whenGivenExistingUserId_apply_shouldThrowsException() {
        ListMembershipRewardsQuery query = ListMembershipRewardsQuery.builder(nonExistingUserId).build();
        ListMembershipRewardsUseCase service = new ListMembershipRewardsService(membershipRepository);
        assertThrows(MembershipNotFoundException.class, () -> service.apply(query));
    }
}