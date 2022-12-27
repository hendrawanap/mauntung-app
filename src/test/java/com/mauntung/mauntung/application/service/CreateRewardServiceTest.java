package com.mauntung.mauntung.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mauntung.mauntung.application.port.membership.MembershipRepository;
import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateRewardServiceTest {
    private static RewardRepository rewardRepository;
    private static MembershipRepository membershipRepository;

    @BeforeAll
    static void beforeAll() {
        rewardRepository = mock(RewardRepository.class);
        membershipRepository = mock(MembershipRepository.class);
    }
    @Test
    void givenRewardRepositorySaveReturnEmpty_apply_shouldThrowsException() throws JsonProcessingException {
        when(rewardRepository.save(any())).thenReturn(Optional.empty());
        when(membershipRepository.findByUserId(any())).thenReturn(Optional.empty());

        CreateRewardCommand command = CreateRewardCommand.builder("name", "desc", "terms", 10, 1).build();
        CreateRewardService service = new CreateRewardService(rewardRepository, membershipRepository);

        assertThrows(RuntimeException.class, () -> service.apply(command));
    }

    @Test
    void givenRewardRepositorySaveReturnLong_apply_shouldReturnResponse() throws JsonProcessingException {
        when(rewardRepository.save(any())).thenReturn(Optional.of(1L));
        when(membershipRepository.findByUserId(any())).thenReturn(Optional.empty());

        CreateRewardCommand command = CreateRewardCommand.builder("name", "desc", "terms", 10, 1).build();
        CreateRewardService service = new CreateRewardService(rewardRepository, membershipRepository);

        assertNotNull(service.apply(command));
    }
}