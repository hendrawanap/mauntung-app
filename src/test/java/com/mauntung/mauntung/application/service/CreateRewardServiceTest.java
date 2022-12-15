package com.mauntung.mauntung.application.service;

import com.mauntung.mauntung.application.port.reward.CreateRewardCommand;
import com.mauntung.mauntung.application.port.reward.CreateRewardResponse;
import com.mauntung.mauntung.application.port.reward.RewardRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateRewardServiceTest {
    @Test
    void givenRewardRepositorySaveReturnEmpty_apply_shouldReturnFailedResponse() {
        RewardRepository rewardRepository = mock(RewardRepository.class);
        when(rewardRepository.save(any())).thenReturn(Optional.empty());

        CreateRewardCommand command = CreateRewardCommand.builder("name", "desc", "terms", 10).build();

        CreateRewardService service = new CreateRewardService(rewardRepository);
        CreateRewardResponse response = service.apply(command);
        assertNotNull(response.getErrorResponse());
        assertNull(response.getSuccessResponse());
    }

    @Test
    void givenRewardRepositorySaveReturnLong_apply_shouldReturnSuccessResponse() {
        RewardRepository rewardRepository = mock(RewardRepository.class);
        when(rewardRepository.save(any())).thenReturn(Optional.of(1L));

        CreateRewardCommand command = CreateRewardCommand.builder("name", "desc", "terms", 10).build();

        CreateRewardService service = new CreateRewardService(rewardRepository);
        CreateRewardResponse response = service.apply(command);
        assertNull(response.getErrorResponse());
        assertNotNull(response.getSuccessResponse());
    }
}