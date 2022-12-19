package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateRewardServiceTest {
    private static RewardRepository rewardRepository;

    @BeforeAll
    static void beforeAll() {
        rewardRepository = mock(RewardRepository.class);
    }
    @Test
    void givenRewardRepositorySaveReturnEmpty_apply_shouldThrowsException() {
        when(rewardRepository.save(any())).thenReturn(Optional.empty());

        CreateRewardCommand command = CreateRewardCommand.builder("name", "desc", "terms", 10).build();
        CreateRewardService service = new CreateRewardService(rewardRepository);

        assertThrows(RuntimeException.class, () -> service.apply(command));
    }

    @Test
    void givenRewardRepositorySaveReturnLong_apply_shouldReturnResponse() {
        when(rewardRepository.save(any())).thenReturn(Optional.of(1L));

        CreateRewardCommand command = CreateRewardCommand.builder("name", "desc", "terms", 10).build();
        CreateRewardService service = new CreateRewardService(rewardRepository);

        assertNotNull(service.apply(command));
    }
}